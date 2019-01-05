
#Title
chartTitle = sprintf("Average Block Creation Time")
set title sprintf("%s", chartTitle) font titleFont
fileName = sprintf('%s.csv','.\results\sim1\results')
fileName1 = sprintf('%s.csv','.\results\sim2\results')

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
set yrange[0 : 30] 

plot \
	fileName using 2:7 \
	with linespoints linestyle 1 \
	title "10miners"
replot \
	fileName1 using 2:7  \
	with linespoints linestyle 2 \
	title "100miners"
	
#set xrange [0:56391403.6908]