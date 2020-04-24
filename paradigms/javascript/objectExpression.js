"use strict"

function PrimitiveOperand() {
}
PrimitiveOperand.prototype.toString = function() {return String(this.value);}
PrimitiveOperand.prototype.postfix = function() {return this.toString();}
PrimitiveOperand.prototype.prefix = function() {return this.toString();}

function Const(value) {
    this.value = value;
}
Const.prototype = new PrimitiveOperand();
Const.prototype.evaluate = function() {return this.value;}
Const.prototype.diff = function() {return constants.zero;}

const constants = {
    one : new Const(1), 
    zero : new Const(0), 
    e : new Const(Math.E)
};

function Variable(name) {
    this.value = name;
}
Variable.prototype = new PrimitiveOperand();
Variable.prototype.evaluate = function(...values) {return values[variables[this.value]];}
Variable.prototype.diff = function(variable) {return (variable === this.value ? constants.one : constants.zero);}
const variables = {'x' : 0, 'y' : 1, 'z' : 2};
const ANY = -1;

function AbstractArithmetic() {}
AbstractArithmetic.prototype.evaluate = function(...values) {
    let evaluatedOperands = this.operands.map(operand => operand.evaluate(...values));   
    return this.operation(...evaluatedOperands);
};
AbstractArithmetic.prototype.diff = function(variable) {
    let diffOps = this.operands.map(operand => operand.diff(variable));
    return this.computeDiff(variable, ...diffOps);
}
AbstractArithmetic.prototype.postfix = function() {return '(' + this.operands.map(operand => operand.postfix()).join(' ') + ' ' + this.operationSign + ')'};
AbstractArithmetic.prototype.prefix = function() {return '(' + this.operationSign + ' ' + this.operands.map(operand => operand.prefix()).join(' ') + ')'};
AbstractArithmetic.prototype.toString = function() {return this.operands.map(operand => operand.toString()).join(' ') + ' ' + this.operationSign};

function factory(sign, operation, computeDiff, ...operands) {
    function Operation(...operands) {
        this.operands = operands;
    }
    Operation.prototype = new AbstractArithmetic();
    Operation.prototype.operationSign = sign;
    Operation.prototype.operation = operation;
    Operation.prototype.computeDiff = computeDiff;
    return new Operation(...operands);
}

function Negate(inner) {
    return factory('negate', 
    (...evaluatedOps) => - evaluatedOps[0], 
    (_, ...diffOps) => new Negate(diffOps[0]), 
    inner);
}

Negate.arity = 1;

function Add(...operands) {
    return factory('+',
    (...evaluatedOps) => evaluatedOps.reduce((accumulator, operand) => accumulator + operand, 0), 
    (_, ...diffOps) => new Add(...diffOps),
    ...operands);
}
Add.arity = 2;

function Subtract(...operands) {
    return factory('-',
    (...evaluatedOps) => evaluatedOps.reduce((accumulator, operand) => accumulator - operand),
    (_, ...diffOps) => new Subtract(diffOps[0], diffOps[1]),
    ...operands);
}
Subtract.arity = 2;

function Multiply(...operands) {
    return factory('*', 
    (...evaluatedOps) => evaluatedOps[0] * evaluatedOps[1],
    (_, ...diffOps) => new Add(new Multiply(operands[0], diffOps[1]), new Multiply(operands[1], diffOps[0])),
    ...operands);
}
Multiply.arity = 2;

function Divide(...operands) {
    return factory('/', 
    (...evaluatedOps) => evaluatedOps[0] / evaluatedOps[1],
    (_, ...diffOps) => new Divide(new Subtract(
        new Multiply(diffOps[0], operands[1]), new Multiply(diffOps[1], operands[0])), 
                                                            new Multiply(operands[1], operands[1])),
    ...operands);
}
Divide.arity = 2;

function Power(...operands) {
    return factory('pow',
    (...evaluatedOps) => Math.pow(evaluatedOps[0], evaluatedOps[1]), 
    (variable) => new Multiply(new Power(operands[0], operands[1]), 
                    new Multiply(operands[1], new Log(constants.e, operands[0])).diff(variable)),
    ...operands);
}
Power.arity = 2;

function Log(...operands) {
    return factory('log', 
    (...evaluatedOps) => Math.log(Math.abs(evaluatedOps[1])) / Math.log(Math.abs(evaluatedOps[0])),
    function (variable, ...diffOps) {
        if (operands[0] instanceof Const) {
            return new Multiply(diffOps[1], new Divide(constants.one, new Multiply(new Log(
                                                        constants.e, operands[0]), operands[1])));
        }
        let leftVal = new Log(constants.e, operands[0]);
        let rightVal = new Log(constants.e, operands[1]);
        return new Divide(rightVal, leftVal).diff(variable);
    },
    ...operands);
}
Log.arity = 2;

function Sumexp(...operands) {
    return factory('sumexp', 
    (...evaluatedOps) => evaluatedOps.reduce((accumulator, operand) => accumulator + Math.pow(Math.E, operand), 0), 
    function(variable) {
        if (operands.length == 0) {
            return constants.zero;
        }
        let left = new Power(constants.e, operands[0]);
        for (let i = 1; i < this.operands.length; i++) {
            left = new Add(left, new Power(constants.e, operands[i]));
        }
        return left.diff(variable);
    }, 
    ...operands
    );
};
Sumexp.arity = ANY;

function Softmax(...operands) {
    return factory('softmax', 
    (...evaluatedOps) => evaluatedOps.length == 0 ? 1 : Math.pow(Math.E, evaluatedOps[0]) 
                        / evaluatedOps.reduce((accumulator, operand) => accumulator + Math.pow(Math.E, operand), 0),
    function(variable) {
        let bottom = new Power(constants.e, operands[0]);
        for (let i = 1; i < operands.length; i++) {
            bottom = new Add(bottom, new Power(constants.e, operands[i]));
        }
        return new Divide(new Power(constants.e, operands[0]), bottom).diff(variable);
    }, 
    ...operands
    );
}
Softmax.arity = ANY;

//exceptions
function WrongInputFormatError(message, index) {
    this.message = message + ' at index ' + index;
}
WrongInputFormatError.prototype.name = "WrongInputFormatError";

function WrongArityError(expected, found, index) {
    this.message = 'Expected ' + expected + ' arguments, found ' + found + ' at index ' + index;
}
WrongArityError.prototype.name = "WrongArityError";

function UnexpectedWordError(message, index) {
    this.message = message + ' at index ' + index;
}
UnexpectedWordError.prototype.name = "UnexpectedWordError";

// parser
const whiteSpaces = ['\t', ' ', '\f', '\n', '\v', '\r'];
const isWhiteSpace = (character) => whiteSpaces.includes(character);
const isDigit = (character) => '0' <= character && character <= '9';
const isInteger = (str) => {        
    let i = (str[0] == '-' ? 1 : 0);
    for (; i < str.length; i++) {
        if (!isDigit(str[i])) {
            return false;
        }
    }
    return true;
}
const isVariable = (str) => str in variables;
const operations = {
    "+" : Add,
    "-" : Subtract,
    "/" : Divide,
    "*" : Multiply,
    "negate" : Negate,
    "pow" : Power,
    "log" : Log,
    "sumexp" : Sumexp, 
    "softmax" : Softmax
};
const getOperation = (str) => operations[str];
function AbstractParserSource (expression, startIndex) {
    this.expression = expression;
    this.index = startIndex;
    this.getChar = function() {return expression[this.index]};
    this.getIndex = function() {return this.index};
}

function PrefixSource (expression) {
    AbstractParserSource.call(this, expression, 0);
}
PrefixSource.prototype.substring = function(beginIndex, endIndex) {return this.expression.substring(beginIndex, endIndex)};
PrefixSource.prototype.next = function() {return this.index++};
PrefixSource.prototype.hasNext = function() {return this.index < this.expression.length};

function PostfixSource(expression) {
    AbstractParserSource.call(this, expression, expression.length - 1);
}
PostfixSource.prototype.substring = function(beginIndex, endIndex) {return this.expression.substring(beginIndex + 1, endIndex + 1)};
PostfixSource.prototype.next = function() {return this.index--};
PostfixSource.prototype.hasNext = function() {return this.index >= 0};

function AbstractParser(startChar, endChar, source) {
    const skipWhitespace = () => { 
        while (isWhiteSpace(source.getChar())) {
            source.next();
        }
    };

    const test = function(expected) {
        if (expected === source.getChar()) {
            source.next();
            return true;
        }
        return false;
    }

    const next = () => {
        skipWhitespace();
        let startIndex = source.getIndex();
        do {source.next()} while (source.hasNext()
                && !isWhiteSpace(source.getChar())
                    && source.getChar() != startChar
                        && source.getChar() != endChar);
        let result = source.substring(startIndex, source.getIndex());
        skipWhitespace();
        if (result.length == 0) {
            throw new WrongInputFormatError('empty input', source.getIndex());
        }
        return result;
    }

    const parseArgument = () => {
        let startIndex = source.getIndex();
        let rawInput = next();
        if (isVariable(rawInput)) {
            return new Variable(rawInput);
        }
        if (isInteger(rawInput)) {
            return new Const(Number(rawInput));
        }
        throw new UnexpectedWordError('Expected number or variable, found ' + rawInput, startIndex);
    }

    const parseOperation = () => {
        let startIndex = source.getIndex();
        skipWhitespace();
        if (!source.hasNext()) {
            throw new WrongInputFormatError('Expected operand or operation, found end', startIndex < 0 ? 0 : startIndex);
        }
        if (!test(startChar)) {
            return parseArgument();
        }
        let opIndex = source.getIndex();
        let rawOperation = next();
        let operation = getOperation(rawOperation);
        if (!operation) {
            throw new UnexpectedWordError('Expected operation, found ' + rawOperation, opIndex);
        }
        let args = []; 
        while (source.hasNext()) {
            skipWhitespace();
            if (test(endChar)) {
                skipWhitespace();
                if (args.length == operation.arity || operation.arity == ANY) {
                    if (args.length == 0) {
                        return new operation();
                    }
                    return new operation(...args);
                }
                throw new WrongArityError(operation.arity, args.length, startIndex);
            }
            args.append(parseOperation());
        }
        throw new WrongInputFormatError('Expected bracket, found end', source.getIndex() < 0 ? 0 : source.getIndex());
    }
    let result = parseOperation();
    skipWhitespace();
    if (source.hasNext()) {
        throw new WrongInputFormatError('Expected end, found ' + source.getChar(), source.getIndex());
    }
    return result;
}
function parseCloses(append, opening, closing, source) {
    Array.prototype.append = append;
    let result = AbstractParser(opening, closing, source);
    delete Array.prototype.append;
    return result;
}
function parsePrefix(expression) {
    return parseCloses(function(x) {this.push(x);}, '(', ')', new PrefixSource(expression));
}
function parsePostfix(expression) {
    return parseCloses(function(x) {this.unshift(x);}, ')', '(', new PostfixSource(expression));
}

///object expression 1

function splitByWhiteSpaces(str) {
    let splited = [];
    for (let i = 0; i < str.length; i++) {
        if (!isWhiteSpace(str[i])) {
            let wordStart = i;
            while (!isWhiteSpace(str[i]) && i < str.length) {
                i++;
            }
            splited.push(str.substring(wordStart, i));
        }
    }
    return splited;
}

function parse(expression) {
    let splited = splitByWhiteSpaces(expression);
    let expressionStack = [];
    for (let i = 0; i < splited.length; i++) {
        if (splited[i] in operations) {
            let operation = operations[splited[i]];
            let variables = expressionStack.splice(expressionStack.length - operation.arity, expressionStack.length);
            expressionStack.push(new operation(...variables));
        } else if (isVariable(splited[i])) {
            expressionStack.push(new Variable(splited[i]));
        } else {
            expressionStack.push(new Const(Number(splited[i])));
        }
    }
    return expressionStack[0];
}