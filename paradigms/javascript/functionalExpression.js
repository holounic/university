"use strict";

const abstractOperation = operation => (...operands) => (...values) => {
    let result = operands.map(operand => operand(...values));
    return operation(...result);
};

const subtract = abstractOperation((x, y) => x - y);
const add = abstractOperation((x, y) => x + y);
const multiply = abstractOperation((x, y) => x * y);
const divide = abstractOperation((x, y) => x / y);
const negate = abstractOperation((x) => -x);

const variable = (name) => (...values) => values[variables.indexOf(name)];
const cnst = (x) => () => x;
const pi = () => Math.PI;
const e = () => Math.E;

const sum = (...operands) => (...values) => {
    let result = 0;
    operands.forEach(operand => result += operand(...values));
    return result;
};

const avg5 = (...args) => (...values) => sum(...args)(...values) / args.length;

const med3 = abstractOperation((...args) => (args.sort((a, b) => a - b))[Math.floor(args.length / 2)]);


const operationInfo = (name, operands) => ({
    "name" : name,
    "operands" : operands
});

const operations = {
    '+' : operationInfo(add, 2),
    '-' : operationInfo(subtract, 2),
    '/' : operationInfo(divide, 2),
    '*' : operationInfo(multiply, 2),
    'negate' : operationInfo(negate, 1),
    'med3' : operationInfo(med3, 3),
    'avg5' : operationInfo(avg5, 5)
};

const mathConsts = {
    "pi" : pi, 
    "e" : e
};

const whiteSpaces = ['\t', ' ', '\f', '\n', '\v', '\r'];
const isWhiteSpace = (character => whiteSpaces.includes(character));
const variables = ['x', 'y', 'z'];

function splitByWhiteSpaces(str) {
    let splited = [];
    for (let i = 0; i < str.length; i++) {
        if (!isWhiteSpace(str[i])) {
            let wordStart = i;
            while (i < str.length && !isWhiteSpace(str[i])) {
                i++;
            }
            splited.push(str.substring(wordStart, i));
        }
    }
    return splited;
}

function parse(expr) {
    let splited = splitByWhiteSpaces(expr);
    let expressionStack = [];
    for (let i = 0; i < splited.length; i++) {
        if (splited[i] in operations) {
            let operation = operations[splited[i]].name;
            let arity = operations[splited[i]].operands;
            let args = expressionStack.splice(expressionStack.length - arity, expressionStack.length);
            expressionStack.push(operation(...args)); 
            continue; 
        }
        if (variables.includes(splited[i])) {
            expressionStack.push(variable(splited[i]));
            continue;
        }
        if (splited[i] in mathConsts) {
            expressionStack.push(mathConsts[splited[i]]);
            continue;
        }
        expressionStack.push(cnst(Number(splited[i])));
    }
    return expressionStack[0];
}