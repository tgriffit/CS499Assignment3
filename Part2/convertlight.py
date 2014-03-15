def main():
	infile = open("part2.arff")
	filelines = infile.readlines()
	
	lightvals = []
	datalines = []
	# find the "@DATA" line, then add 1 to get the first comma seperated list of data
	datastart = filelines.index("@DATA\n") + 1
	for line in filelines[datastart:]:
	    dataline = line.strip().split(',');
	    datalines.append(dataline)
	    lightvals.append(dataline[2])
	
	'''
	27-25	=> 0
	30-28	=> 20
	34-31	=> 40
	39-35	=> 60
	42-40	=> 80
	45-43	=> 100
	'''
	
	outfile = open("convertedlight.arff", "w")
	# print the converted file back out to an arff
	for line in filelines[:datastart]:
		print>>outfile, line.strip()
	for x, y, l in datalines:
		light = int(l)
		line = [x, y]
		
		# quantize the light value data
		if light <= 45 and light >= 43:
			line.append("100")
		elif light <= 42 and light >= 40:
			line.append("80")
		elif light <= 39 and light >= 35:
			line.append("60")
		elif light <= 34 and light >= 31:
			line.append("40")
		elif light <= 30 and light >= 28:
			line.append("20")
		else:
			line.append("0")
		
		print>>outfile, ",".join(line)
	
	outfile.close()
	infile.close()

if __name__ == "__main__":
	main()
