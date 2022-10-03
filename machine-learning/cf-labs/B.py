def out(d):
    print('%.9f'%d)


n = int(input())
confusion = [[int(x) for x in input().split()] for _ in range(n)]
p, r, f = [0 for _ in range(n)], [0 for _ in range(n)], [0 for _ in range(n)]
f_w, p_w, r_w = .0, .0, .0
f_reg = .0
tp, fp, fn = .0, .0, .0

total = sum([sum(row) for row in confusion])
for i in range(n):
    row_sum = sum(confusion[i])
    col_sum = sum(confusion[j][i] for j in range(n))
    p[i] = confusion[i][i] / col_sum if not col_sum == 0 else 0
    r[i] = confusion[i][i] / row_sum if not row_sum == 0 else 0
    f[i] = (0 if p[i] + r[i] == 0 else 2 * p[i] * r[i] / (p[i] + r[i]))
    f_w += row_sum * f[i]
    p_w += row_sum * p[i]
    r_w += row_sum * r[i]
    f_reg += f[i]
    tp += confusion[i][i] * row_sum
    fp += (col_sum - confusion[i][i]) * row_sum
    fn += (row_sum - confusion[i][i]) * row_sum

p_w /= total
r_w /= total
f_regular = f_w / total
tp /= total
fp /= total
fn /= total

f_micro = 0 if (tp + 0.5 * (fp + fn)) == 0 else tp / (tp + 0.5 * (fp + fn))
f_macro = (0 if r_w + p_w == 0 else 2 * p_w * r_w / (p_w + r_w))
out(f_micro)
out(f_macro)
out(f_regular)
