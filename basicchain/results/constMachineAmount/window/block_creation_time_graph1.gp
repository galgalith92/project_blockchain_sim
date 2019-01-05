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
fileName1000 = sprintf('%s.csv','.\window1000\results')
fileName2000 = sprintf('%s.csv','.\window2000\results')
fileName3000 = sprintf('%s.csv','.\window3000\results')
fileName4000 = sprintf('%s.csv','.\window4000\results')
fileName5000 = sprintf('%s.csv','.\window5000\results')

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
#set xrange[0 : 7725024] 
#set yrange[0 : 30] 
set xrange[0 : 600] 
plot \
	fileName5000 using 1:7 \
	with linespoints linestyle 1 \
	title "1000 blocks"
#replot \
#	fileName2000 using 1:7  \
#	with linespoints linestyle 2 \
#	title "2000 blocks"
#replot \
#	fileName3000 using 1:7  \
#	with linespoints linestyle 3 \
#	title "3000 blocks"
#replot \
#	fileName4000 using 1:7  \
#	with linespoints linestyle 4 \
#	title "4000 blocks"
#replot \
#	fileName5000 using 1:7  \
#	with linespoints linestyle 5 \
#	title "5000 blocks"

#set xrange [0:56391403.6908]