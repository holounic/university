def read():
    return [[int(x), i] for i, x in enumerate(input().split())]

_, _, [k, _] = read()
c = read()

c.sort()

buckets = [[] for _ in range(k)]
ptr = 0
while ptr < len(c):
    bucket_ptr = 0
    buckets[ptr % k].append(c[ptr][1] + 1)
    ptr += 1
for bucket in buckets:
    print(len(bucket), *bucket)