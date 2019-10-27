import copy

def present(matrix):
    output_file.write('1\n')
    output_file.write(str(len(matrix)) + ' ' + str(len(matrix[0])) + '\n')
    for row in matrix:
        output_file.write(' '.join([str(elem) for elem in row]))
        output_file.write('\n')

def transpose(matrix):
    new_matrix = []
    for i in range(len(matrix)):
        temp = []
        for j in range(len(matrix[0])):
            temp.append(matrix[j][i])
        new_matrix.append(temp)
    return new_matrix

def number_multiplication(number, matrix):
    new_matrix = copy.deepcopy(matrix)
    for i in range(len(matrix)):
        for j in range(len(matrix[0])):
            new_matrix[i][j] *= number
    return new_matrix

def multiplication_is_possible(matrix_one, matrix_two):
    return len(matrix_one[0]) == len(matrix_two)

def matrix_multiplication(matrix_one, matrix_two):
    if not multiplication_is_possible(matrix_one, matrix_two):
        output_file.write(str(0))
        output_file.close()
        exit()

    temp_matrix_one = copy.deepcopy(matrix_one)
    temp_matrix_two = copy.deepcopy(matrix_two)
    temp = []
    new_matrix = []
    current_sum = 0
    for z in range(len(temp_matrix_one)):
        for j in range(len(temp_matrix_two[0])):
            for i in range(len(temp_matrix_one[0])):
                current_sum += temp_matrix_one[z][i] * temp_matrix_two[i][j]
            temp.append(current_sum)
            current_sum = 0
        new_matrix.append(temp)
        temp = []
    return new_matrix

def sum_is_possible(matrix_one, matrix_two):
    return len(matrix_one) == len(matrix_two) and len(matrix_one[0]) == len(matrix_two[0])

def matrix_sum(matrix_one, matrix_two):
    if not sum_is_possible(matrix_one, matrix_two):
        output_file.write(str(0))
        output_file.close()
        exit()
    new_matrix = copy.deepcopy(matrix_one)
    for i in range(len(matrix_one)):
        for j in range(len(matrix_two[0])):
            new_matrix[i][j] += matrix_two[i][j]
    return new_matrix

def fill_matrix(m):
    if m == 0:
        return []
    matrix = []
    temp = []
    for i in [j for j in input_file.readline().split()]:
        temp.append(float(i))
        if len(temp) == m:
            matrix.append(temp)
            temp = []
    return matrix

def expression(a, b, A, B, C, D, F):
    mid_one = matrix_sum(number_multiplication(a, A), number_multiplication(b, transpose(B)))
    mid_two = matrix_multiplication(C, transpose(mid_one))
    mid_three = matrix_multiplication(mid_two, D)
    result = matrix_sum(mid_three, number_multiplication(-1, F))
    return result
      


input_file = open("input.txt", "r")
output_file = open("output.txt", "w")

a, b = map(float, input_file.readline().split())

nA, mA = map(int, input_file.readline().split())
A = fill_matrix(mA)

nB, mB = map(int, input_file.readline().split())
B = fill_matrix(mB)

nC, mC = map(int, input_file.readline().split())
C = fill_matrix(mC)

nD, mD = map(int, input_file.readline().split())
D = fill_matrix(mD)

nF, mF = map(int, input_file.readline().split())
F = fill_matrix(mF)

input_file.close()

result = expression(a, b, A, B, C, D, F)
present(result)
output_file.close()

