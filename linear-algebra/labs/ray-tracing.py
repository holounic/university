input_file = 'input.txt'
output_file = 'output.txt'

class Form():

    def __init__(self, A, B, C):
        self.A = (B.y - A.y) * (C.z - A.z) - (C.y - A.y) * (B.z - A.z)
        self.B = -((B.x - A.x) * (C.z - A.z) - (C.x - A.x) * (B.z - A.z))
        self.C = (B.x - A.x) * (C.y - A.y) - (C.x - A.x) * (B.y - A.y)
        self.D = -A.x * self.A - A.y * self.B - A.z * self.C
        self.a = A
        self.b = B
        self.c = C

    def get(self):
        return Vector(self.A, self.B, self.C)

    def point_intersection(self, line):
        temp = (self.A * line.vector.x + self.B * line.vector.y + self.C * line.vector.z)
        if temp == 0:
            return None
        t = -(self.A * line.point.x + self.B * line.point.y + self.C * line.point.z + self.D) / temp
        if t <= 0:
            return None
        return Vector(line.vector.x * t + line.point.x, line.vector.y * t + line.point.y,
                      line.vector.z * t + line.point.z)


class Vector():

    def __init__(self, x, y, z):
        self.x = x
        self.y = y
        self.z = z

    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y, self.z + other.z)

    def __sub__(self, other):
        return Vector(self.x - other.x, self.y - other.y, self.z - other.z)

    def __mul__(self, other):
        if type(other) == type(self):
            return self.x * other.x + self.y * other.y + self.z * other.z
        else:
            return Vector(self.x * other, self.y * other, self.z * other)

    def __str__(self):
        return str(self.x) + " " + str(self.y) + " " + str(self.z)

    def len(self):
        return pow(sum(map(lambda x : x * x, [self.x, self.y, self.z])), 0.5)

    def len_2(self):
        return sum(map(lambda x: x * x, [self.x, self.y, self.z]))

    def projection_on(self, b):
        return b * ((self * b) / (b.len_2()))


class Line():

    def __init__(self, a, v):
        self.vector = v
        self.point = a

    def reflect_from(self, plane):
        intersection = plane.point_intersection(self)
        if intersection is None:
            return None
        perpendicular = plane.get()
        v_vert = self.vector.projection_on(perpendicular) * (-1)
        v_hor = self.vector + v_vert
        return Line(intersection, v_vert + v_hor)


def main():
    with open(input_file, 'r') as file_in:
        A = Vector(*map(float, file_in.readline().strip().split()))
        B = Vector(*map(float, file_in.readline().strip().split()))
        C = Vector(*map(float, file_in.readline().strip().split()))
        D = Vector(*map(float, file_in.readline().strip().split()))
        temp = Vector(*map(float, file_in.readline().strip().split()))
        beam = Line(Vector(*map(float, file_in.readline().strip().split())),
                    temp)
        e = int(file_in.readline().strip())
        n = int(file_in.readline().strip())
        mirrors = []
        vx = B - A
        vz = D - C
        faces = [Form(A, B, C),
                 Form(D, B, C),
                 Form(A, B, B + vz),
                 Form(A, C - vx, D - vx),
                 Form(C - vx, C, D),
                 Form(A + vz, B + vz, D)
                 ]
        for i in range(n):
            f = Vector(*map(float, file_in.readline().strip().split()))
            s = Vector(*map(float, file_in.readline().strip().split()))
            t = Vector(*map(float, file_in.readline().strip().split()))
            mirrors.append(Form(f, s, t))

    while e > 0:
        res = [-1, None, -1]
        for mirror in mirrors:
            temp = beam.reflect_from(mirror)
            if temp is not None:
                dist = (temp.point - beam.point).len()
                if res[0] == -1 or res[0] > dist > 0:
                    res = [dist, temp, 0]

        for face in faces:
            temp = beam.reflect_from(face)
            if temp is not None:
                dist = (temp.point - beam.point).len()
                if res[0] == -1 or res[0] > dist > 0:
                    res = [dist, temp, 1]

        if res[2] == -1:
            return 'trolling occured'
        elif res[2] == 1:
            return '1\n' + str(e) + '\n' + str(res[1].point) + '\n' + str(beam.vector)
        else:
            e -= 1
            beam = res[1]
    return '0\n' + str(beam.point)


with open(output_file, 'w') as file_out:
    file_out.write(main())
