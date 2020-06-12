(defn constant [value] (constantly value))
(defn variable [name] (fn [args] (get args name)))

(defn operation [op] 
  (fn [& ops] 
    (fn [vars] (apply op ((apply juxt ops) vars)))))

(def subtract (operation -))
(def negate subtract)
(def add (operation +))
(def divide (operation (fn ([x] (/ (double x))) 
                           ([x & args] (/ x (double (apply * args)))))))   
(def multiply (operation *))                                 
(def med (operation (fn [& args] (nth (sort args) (quot (count args) 2)))))
(def avg (operation (fn [& args] (/ (apply + args) (count args)))))

(defn proto-get [obj key]
  (cond
    (contains? obj key) (obj key)
    (contains? obj :proto) (proto-get (obj :proto) key)
    :else nil))

(defn proto-call [this key & args]
  (apply (proto-get this key) this args))

(defn field [key] #(proto-get %1 key))

(defn method [key] #(apply proto-call %1 key %&))

(defn base-constructor [constr proto]
  (fn [& args] (apply constr {:proto proto} args)))

(defn common-constructor [proto & fields]
  (base-constructor (fn [this & values]
                 (reduce (fn [this [field value]]
                           (assoc this
                             field value)) this
                         (mapv vector fields values)))
               proto))

(def evaluate (method :evaluate))
(def diff (method :diff))
(def toString (method :toString))

(declare NULL)

(def ConstantProto
  (let [val (field :value)]
    {
      :evaluate         (fn [this vals] (val this))
      :toString         #(format "%.1f" (double (val %1)))
      :diff             (fn [this diff-var] NULL)}))

(def Constant (common-constructor ConstantProto :value))

(def NULL (Constant 0))
(def ONE (Constant 1))

(def VariableProto
  (let [name (field :name)]
    {
      :evaluate (fn [this values] (values (name this)))
      :toString #(name %1)
      :diff #(if (not (= (name %1) %2)) NULL ONE)}))

(def Variable (common-constructor VariableProto :name))

(def OperationProto
  (let [name (field :name) f (field :f) diff-func (field :diff-func) inner (field :inner)]
    {
      :evaluate (fn [this values] (apply (f this) (mapv #(evaluate % values) (inner this))))
      :diff (fn [this diff-var] ((diff-func this) (inner this) (mapv #(diff % diff-var) (inner this))))
      :toString #(str "(" (name %1) " " (clojure.string/join " " (mapv toString (inner %1)))")")}))

(declare Multiply)

(defn OperationConstr [name f diff-func]
  (base-constructor
              #(assoc %1 :inner %&)
              ((common-constructor OperationProto :name :f :diff-func) name f diff-func)))

(def Negate (OperationConstr
              'negate
              -
              (fn [_ [inner-diff]] (Negate inner-diff))))

(def Subtract (OperationConstr
              '-
              -
              #(apply Subtract %2)))

(def Add (OperationConstr
          '+
          +
          #(apply Add %2)))


(defn mul-diff [inner diff-var]
  (nth (reduce (fn [[u du] [v dv]]
                    [(Multiply u v)
                     (Add (Multiply du v)
                          (Multiply u dv))])
                  [ONE NULL]
                  (mapv vector inner diff-var)) 1))

(def Multiply (OperationConstr
                '*
                *
                mul-diff))

(def Divide (OperationConstr
              '/
              (fn ([u] (/ (double u))) 
                  ([first & next] (/ first (double (apply * next)))))
              (fn [[u & inner] [du & diff-var]]
                (if (empty? inner)
                  (Divide (Negate du)
                          (Multiply u u))
                  (let [v (apply Multiply inner)
                        dv (mul-diff inner diff-var)]
                    (Divide (Subtract (Multiply du v) (Multiply u dv)) (Multiply v v)))))))

(def Sum (OperationConstr
           'sum
           +
           #(apply Sum %2)))

(def Avg (OperationConstr
           'avg
           #(/ (apply + %&) (count %&))
           #(Divide (apply Add %2) (Constant (count %1)))))

(def ops-func {
              'negate negate
              '- subtract
              '+ add                    
              '* multiply
              '/ divide
              'avg avg
              'med med
            })

(def ops-obj {
              'negate Negate
              '- Subtract
              '+ Add
              '* Multiply
              '/ Divide
              'sum Sum
              'avg Avg
              })

(defn parse-expression [ops constant variable expr]
  (cond
    (seq? expr) (apply (ops (first expr))
                             (mapv (partial parse-expression ops constant variable) (rest expr)))
    (number? expr) (constant expr)
    (symbol? expr) (variable (str expr))))

(defn abstract-parser [ops const var] #(parse-expression ops const var (read-string %1)))

(def parseFunction (abstract-parser ops-func constant variable))
(def parseObject   (abstract-parser ops-obj  Constant Variable))
