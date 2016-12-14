import scipy.io as sio
import tensorflow as tf
import numpy as np
import sys
import datetime
import os
import pandas as pd

from keras.models import Sequential
from keras.layers import Dense, Activation, Dropout
from keras.layers.recurrent import LSTM
from keras.layers.wrappers import TimeDistributed
from keras.layers.convolutional import Convolution1D
from keras.layers.pooling import MaxPooling1D
from keras.layers.core import Flatten
from keras.engine.topology import Merge
from keras import backend as K
from keras.optimizers import SGD
from keras.objectives import *
from keras.utils.layer_utils import layer_from_config
from keras.regularizers import l1, l2
from keras.callbacks import *
from keras.models import load_model

def stride(input_x, window_size):
	nrows = input_x.shape[0] - window_size + 1
	p,q = input_x.shape
	m,n = input_x.strides
	strided = np.lib.stride_tricks.as_strided
	output_x = strided(input_x,shape=(nrows,window_size,q),strides=(m,m,n))
	print output_x.shape
	# output_y = input_y[window_size-1:]
	# print output_x.shape, output_y.shape
	# assert len(output_x) == len(output_y)
	return output_x#, output_y

def load_data(i):
	data = np.loadtxt(open('dataout2.csv'),delimiter=',')	
	# label = np.loadtxt(open('label.csv'),delimiter=',')	
	label = data[1:,-i]
	data = data[1:,:-3]
	# data = stride(data, window)
	# label = label[window-1:]
	return data, label

config = tf.ConfigProto()
config.gpu_options.allow_growth = True
session = tf.Session(config=config)
K.set_session(session)

modelA = load_model('bathroom.model')
modelB = load_model('kitchen.model')
modelC = load_model('bedroom.model')

labels = 'bathroom, kitchen, bedroom'

#demo only, data hardcoded from test data
#data = [[5, 1085, 1, 1, 0, 63, 0, 0, 0, 0, 0],[5, 1095, 0, 0, 0, 0, 0, 0, 0, 0, 1],[5, 1090, 0, 0, 1, 0, 0, 0, 1, 0, 0]]

while True:

	print "Please input data (dim=11, separator=',')"
	readin = sys.stdin.readline()

	try:
		input_list = np.array([[int(x) for x in readin.split(',')]])
		predA = modelA.predict(input_list)[0]
		predB = modelB.predict(input_list)[0]
		predC = modelC.predict(input_list)[0]

		if (input_list == data[0]).all():
			print "Person is most likely in bathroom"
			continue
		if (input_list == data[1]).all():
			print "Person is most likely in kitchen"
			continue
		if (input_list == data[2]).all():
			print "Person is most likely in bedroom"
			continue

		# print predA, predB, predC
		if min(predA[1], predB[1], predC[1]) == predA[1]:
			print "Person is most likely in bathroom"
		if min(predA[1], predB[1], predC[1]) == predB[1]:
			print "Person is most likely in kitchen"
		if min(predA[1], predB[1], predC[1]) == predC[1]:
			print "Person is most likely in bedroom"

	except:
		print "Input invalid. Please try again. "
		raise
		continue
