reset
# csv
set datafile separator ","
csvDirString = ""

#str2num
str2num(s)=s+0.0

#Fonts
labelsFont = ",12"
titleFont = ",18"
ticsFont = ",12"
 
#Title
chartTitle = sprintf("Average Block Creation Time")
set title sprintf("%s", chartTitle) font titleFont
fileName1 = sprintf('%s.csv','.\results1')
fileName2 = sprintf('%s.csv','.\results2')
fileName3 = sprintf('%s.csv','.\results3')

set xlabel 'Window Number' font labelsFont
set ylabel "Average Block Creation Time \n [minutes]" font labelsFont
set xtics font ticsFont
set ytics font ticsFont

# actual plotting 
set timefmt "%s"
set format x "%s"
set xdata time

set mxtics
set mytics
set style line 12 lc rgb '#ddccdd' lt 1 lw 1.5
set style line 13 lc rgb '#ddccdd' lt 1 lw 0.5
set grid xtics mxtics ytics mytics back ls 12, ls 13

# Axis ranges 
#set xrange[0 : 600] 
set yrange[0 : 60] 

plot \
	fileName1 \
	using 1:2 with \
	linespoints linestyle 1 \
	title "10 random variables"
replot \
	fileName2 \
	using 1:2 with \
	linespoints linestyle 2 \
	title "100 random variables"
#replot \
#	fileName3 \
#	using 1:2 with \
#	linespoints linestyle 3 \
#	title "100000 random variables"
	
#set xrange [GPVAL_X_MIN:GPVAL_X_MAX]
#set yrange [GPVAL_Y_MIN:GPVAL_Y_MAX]