import sys
import os
import matplotlib.pyplot as plt

# fn = sys.argv[1]
fns = os.listdir('csv/')

for fn in fns:
	f = open('csv/'+fn)
	lines = f.readlines()
	f.close()

	epoch = [int(l.split(',')[0]) for l in lines[1:]]
	loss = [float(l.split(',')[-1]) for l in lines[1:]]

	plt.plot(epoch, loss)
plt.show()
