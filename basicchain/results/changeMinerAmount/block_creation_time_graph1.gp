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
fileName = sprintf('%s.csv','.\add_miner\results')
fileName1 = sprintf('%s.csv','.\remove_miner\results')

set xlabel 'Simulation Time [minutes]' font labelsFont
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
set xrange[0 : 7725024] 
set yrange[0 : 60] 

plot \
	fileName using 2:7 \
	with linespoints linestyle 1 \
	title "add 8000 machines"
replot \
	fileName1 using 2:7  \
	with linespoints linestyle 2 \
	title "remove 8000 machines"
	
#set xrange [0:56391403.6908]