~take=18;
~num=411;
~dyad="7";
// Read Files
(
	// LEFT CHANNEL
	~path = "/home/aucotsi/data/onsetsData/onsetsDyads/dyads_LR_411";
	a = PopulatedData(~path ++ "/dyad" ++ ~dyad ++ "_L." ++ ~take.asString ++ "-", ~num);
	a.iddict.size.postln;
	// RIGHT CHANNEL
	b = PopulatedData(~path ++ "/dyad" ++ ~dyad ++ "_R." ++ ~take.asString ++ "-", ~num);
	b.iddict.size;
)
///
(
	"\n\n\n\n\n\n".post;
	~min = 40; //
	// LEFT CHANNEL
	c = RemoveNoise(a.array, 0.065); // 60ms
 	c.removeOutliers(~min);
	~leftCHANNEL = c.outputArray;
	"\n".post;
	"LEFT CHANNEL: ".post; ~leftCHANNEL.size.postln;
	// RIGHT CHANNEL
	d = RemoveNoise(b.array, 0.065); // 60ms
	d.removeOutliers(~min);
    	~rightCHANNEL = d.outputArray;
	"\n".post;
	"RIGHT CHANNEL: ".post; ~rightCHANNEL.size.postln;
)
// REMOVE IDENTICAL ELEMETS FROM LR-CHANNELS (similar to RmoveMetronomeToo)
(
	n = RemoveIdentical( ~leftCHANNEL, ~rightCHANNEL, 0.030 ); // 25 ms
	n.arrayLeft;
	"n.arrayLeft.size: ".post; n.arrayLeft.size.postln;
	n.arrayRight;
	"n.arrayRight.size: ".post; n.arrayRight.size;
)
// MERGE ONSETS
(
	k = RemoveNoise(n.arrayLeft, 0.120);
	k.mergeOnsets.size;
	l = RemoveNoise(n.arrayRight, 0.120);
	l.mergeOnsets.size;
)
// IOI
(
	v = FilteredIOI(k.mergeOnsets);
	v.outputArray.size;
	w = FilteredIOI(l.mergeOnsets);
	w.outputArray.size;
)
//
(
	f = FilteredIOI(v.outputArray);
	f.outputArray.size;
	g = FilteredIOI(w.outputArray);
	g.outputArray.size;
)
// Run this multiple times if needed
(
	f = FilteredIOI(f.outputArray);
	f.outputArray.size;
	g = FilteredIOI(g.outputArray);
	g.outputArray.size;
)
// GNUPLOT (plot signal)
(
	~pathLEFT = "/home/aucotsi/data/waveFilesNoMetro/Marc_Dyad_0" ++ ~dyad ++ "_"++ ~take.asString ++".L.wav";
	~pathRIGHT = "/home/aucotsi/data/waveFilesNoMetro/Marc_Dyad_0" ++ ~dyad ++ "_"++ ~take.asString ++".R.wav";
	~leftCHANNEL = PlotOnsets(~pathLEFT);
	~rightCHANNEL = PlotOnsets(~pathRIGHT);
)
// plot onsets
(
	~leftCHANNEL.plotOnsets(f.outputArray, "set title \"Left Channel\"");
	~rightCHANNEL.plotOnsets(g.outputArray, "set title \"Right Channel\"");
)
//
SaveOnsets(f.outputArray, g.outputArray, ~dyad, ~take, "/home/aucotsi/code/orgmode/research/cmmr2015/onsets/")
