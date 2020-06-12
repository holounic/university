(defn equal-vect-len [& v] (apply = (mapv count v)))
(defn equal-matr-dim [& m] (equal-vect-len (mapv first m)))
(defn equal-ten-dim [ts] (apply = (mapv count ts)))

(defn is-vector [a] (and (every? number? a) (vector? a)))
(defn is-matrix [a] (and (every? is-vector a) (vector? a) (apply equal-vect-len a)))
(defn is-tensor [& ts] (or (every? number? ts) (and (every? vector? ts) (equal-ten-dim ts)
                                                      (apply is-tensor (apply concat [] ts)))))

(defn v [f] (fn [& vectors] {
    :pre  [(every? is-vector vectors) (apply equal-vect-len vectors)]
    :post [(is-vector %) (equal-vect-len (first vectors) %)]
  }
  (apply (partial mapv f) vectors))
)

(def v+ (v +))
(def v- (v -))
(def v* (v *))

(defn v*s [v & s] {
    :pre [(is-vector v) (every? number? s)]
    :post [(is-vector %) (equal-vect-len % v)]
  }
  (mapv (partial * (apply * s)) v)
)

(defn vect [& vectors] {
    :pre  [(every? is-vector vectors) (apply equal-vect-len vectors) (= 3 (count (nth vectors 0)))]
    :post [(is-vector %) (= 3 (count %))]
  }
  (reduce (fn [v1 v2] (vector (- (* (nth v1 1) (nth v2 2)) (* (nth v1 2) (nth v2 1)))
                             (- (* (nth v1 2) (nth v2 0)) (* (nth v1 0) (nth v2 2)))
                             (- (* (nth v1 0) (nth v2 1)) (* (nth v1 1) (nth v2 0))))) vectors))

(defn scalar [& vectors] {
    :pre [(every? is-vector vectors) (apply equal-vect-len vectors)]
    :post [(number? %)]
  }
  (apply + (apply v* vectors))
)

(defn m [f] (fn [& matrixes] {
    :pre  [(every? is-matrix matrixes) (apply equal-matr-dim matrixes)]
    :post [(is-matrix %) (equal-matr-dim % (nth matrixes 0))]
  }
  (apply (partial mapv f) matrixes))
)

(def m+ (m v+))
(def m- (m v-))
(def m* (m v*))

(defn transpose [matrix] {
    :pre  [(is-matrix matrix)]
    :post [(is-matrix %) (= (count matrix) (count (nth % 0))) (= (count %) (count (nth matrix 0)))]
  }
  (apply mapv vector matrix)
)

(defn m*s [m & s] {
    :pre [(every? number? s) (is-matrix m)]
    :post [(is-matrix %) (equal-matr-dim % m)]
  }
  (mapv (fn [v] (apply (partial v*s v) s)) m)
)

(defn m*v [m & v] {
    :pre  [(is-matrix m) (every? is-vector v) (mapv (fn [x] (equal-vect-len v x)) m)]
    :post [(is-vector %) (equal-vect-len % m)]
  }
  (mapv (fn [a] (apply scalar a v)) m)
)

(defn m*m [& matrixes] {
    :pre  [every? is-matrix matrixes]
    :post [(= (count %) (count (nth matrixes 0))) (= (count (nth % 0)) (count (nth (last matrixes) 0)))]
  }
  (reduce (fn [x y] (mapv (fn [v] (mapv (fn [w] (apply + (v* v w))) (transpose y))) x)) matrixes)
)

(defn t [f]
  (letfn [(compute [& tensors]
    {
      :pre [(and (apply is-tensor tensors) 
      (or (and (every? vector? tensors) (equal-ten-dim tensors)) (every? number? tensors)))]
      :post [(is-tensor %)]
    }
      (cond (every? number? tensors)
        (apply f tensors)
          :else
            (apply mapv compute tensors))
    )
  ]
  :return compute)
)

(def t+ (t +))
(def t- (t -))
(def t* (t *))