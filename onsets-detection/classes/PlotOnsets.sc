PlotOnsets {

	var <file;
	var <>array;
	var <>scaling;
	var <>gnu;
	var <>data;
	var <>samples;
	var <>sampleRate = 48000;

	*new { | path, onsets, title |
		^super.new.init(path, onsets, title)
	}

	init { | path, onsets, title |
		this.loadSoundfile(path);
		this.plotSoundfile;
		// this.plotOnsets(onsets);
	}

	loadSoundfile { | path |
		file = SoundFile.openRead(path.standardizePath);
		"file path: ".post; file.path.postln;
		array = FloatArray.newClear(file.numFrames);
		//"array = ".post; array.postln;
		scaling = file.numFrames / sampleRate;
		file.readData(array);
		file.close; // close the file
	}

	plotSoundfile { | onsets |
		var arr = [];
		samples = 2**15;

		scaling = samples / (file.numFrames/sampleRate);
		"scaling: ".post; scaling.postln;

		gnu = GNUPlot.new;
		arr = array.resamp1(samples);
		gnu.setXrange(0, samples);
		gnu.setYrange(-1.0,1.0);
		// plot to GNUPLOT
		gnu.plot2d(arr.clump(1));
		//gnu.plot(onsets);

	}

	plotOnsets { | onsets, title="set title \"Onsets landed\"" |
		var point;
		var relWeight;
		// set label <i> "" at <x>,<y> point pointtype <n>
		// set object circle at <x>,<y> size <r>
		gnu.sendCmd(title);
		//gnu.sendCmd("Theads = 'heads size 0.5,90 front ls 201'");
		onsets.size do: { |i|
			point = onsets[i][0] * scaling;
			relWeight = onsets[i][1] / 411 / 2;
			// point.postln;
			//gnu.sendCmd("set object circle rgb \"black\" at " ++ point.asString ++ ",0.9 size 32 fillcolor rgb \"black\" front");
			// with points pt 7 lc rgb "black"
			gnu.sendCmd("set arrow from " ++point.asString++ ",-0.5 to " ++point.asString++ "," ++ (0.5+relWeight).asString);

		};
		gnu.sendCmd("replot");

	}

}