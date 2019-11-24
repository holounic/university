import math

def dist(a):
    return math.sqrt(sum(map(lambda x: x * x, a)))

def scalar_prod(a, b):
    return sum(map(lambda x, y : x * y, a, b))

def compute_angle(a, b):
    temp = scalar_prod(a, b) / (dist(a) * dist(b))
    temp = math.acos(temp) * 180
    return  temp / math.pi

def vector_mult(a, b):
    res = [None] * 3
    res[0] = a[1]*b[2] - a[2]*b[1]
    res[1] = a[2]*b[0] - a[0]*b[2]
    res[2] = a[0]*b[1] - a[1]*b[0]
    return res


input_file = open('input.txt', 'r')
output_file = open('output.txt','w')
v = map(float, input_file.readline().split())
a = map(float, input_file.readline().split())
m = map(float, input_file.readline().split())
w = map(float, input_file.readline().split())


base = [0, 0, 1]
right_arm = vector_mult(a, base)
left_arm = vector_mult(base, a)
m_ang = compute_angle(base, m)

bad_guy = map(lambda x, y: x - y, w, v)

right_ang = compute_angle(right_arm, bad_guy)
left_ang = compute_angle(left_arm, bad_guy)
    

if left_ang > 60 and right_ang > 60 or m_ang > 60:
    output_file.write(str(0) + "\n")
    output_file.write(";))))))))")  
    exit()

temp = vector_mult(a, base)
if left_ang <= 60:
    output_file.write(str(1) + "\n")
    if compute_angle(a, bad_guy) > 90:
        left_ang *= -1
    if compute_angle(m, temp) > 90:
        m_ang *= -1
    output_file.write(str(left_ang) + "\n")
else:
    output_file.write(str(-1) + "\n")
    if compute_angle(a,bad_guy) > 90:
                right_ang  *= -1
    if compute_angle(m,temp)< 90:
                m_ang *= -1
    output_file.write(str(right_ang) + "\n")
output_file.write(str(m_ang) + "\n")
output_file.write(";))))))))")

