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
fileName1 = sprintf('%s.csv','.\1000machines\results')
fileName2 = sprintf('%s.csv','.\10000000machines\results')


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
	fileName1 using 1:7 \
	with linespoints linestyle 1 \
	title "1000 machines per miner"
replot \
	fileName2 using 1:7  \
	with linespoints linestyle 2 \
	title "10000000 machines per miner"

#set xrange [0:56391403.6908]