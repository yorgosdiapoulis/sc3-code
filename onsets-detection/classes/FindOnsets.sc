FindOnsets { // DIVIDE THE FLOW ON LEFT & RIGHT CHANNEL AND MERGE ON REMOVEIDENTICAL
	var <take;
	var <numOfFiles;
	var <>dataLeft, <>dataRight;
	var <>weightsLeft, <>weightsRight;
	var <>onsetsLeft, <>onsetsRight;
	var <>ioi;
	var <>threshold=0.150; // 100ms
	var counter = 0;
	var <>plotLeft, <>plotRight;
	// DIVIDE THE FLOW ON LEFT & RIGHT CHANNEL AND MERGE ON REMOVEIDENTICAL
	*new { | path, dyad, take, numOfFiles, window=0.55, removeNoise=25, diff=0.125 |
		^super.new.init(path, dyad, take, numOfFiles, window, removeNoise)
	}

	init { | path, dyad, take, numOfFiles, window=0.55, removeNoise=25, diff=0.125 |
		this.populateData(path, dyad, take, numOfFiles, window, removeNoise, diff);
		// this.computeOnsets(path, dyad, take, numOfFiles, window, removeNoise)
	}

	populateData { | path, dyad, take, numOfFiles, window=0.55, removeNoise=25, diff=0.125 |
		var pathLeft, pathRight;
		// path = "/path/to/root/folder/", dyad="dyad7", numOfFiles=405
		pathLeft = path ++ dyad ++ "_L_" ++ numOfFiles.asString ++ "/" ++ dyad ++ "_L." ++ take.asString ++ "-";
		pathRight = path ++ dyad ++ "_R_" ++ numOfFiles.asString ++ "/" ++ dyad ++ "_R." ++ take.asString ++ "-";

		//
		{
			dataLeft = PopulatedDataPatch(pathLeft, numOfFiles);
			dataRight = PopulatedDataPatch(pathRight, numOfFiles);

			if ( (dataLeft.notNil) && (dataRight.notNil) ){
				this.weightedOnsets(dataLeft.array, dataRight.array, window, removeNoise, diff);
			}
		}.defer;
	}

	weightedOnsets { | dataLeft, dataRight, window=0.55, removeNoise=25, diff=0.125 |
		weightsLeft = WeightedOnsets(dataLeft, window);
		weightsRight = WeightedOnsets(dataRight, window);

		"weightsLeft: ".post; weightsLeft.removeOutliers(removeNoise).postln;
		"weightsRight: ".post; weightsRight.removeOutliers(removeNoise).postln;
		//
		this.removeIdentical(weightsLeft.removeOutliers(removeNoise), weightsRight.removeOutliers(removeNoise), diff);
	}

	removeIdentical{ | idLeft, idRight, diff=0.125 |
		// diff is in seconds => 100ms for 0.100
		var uniqueOnsets;
		
		uniqueOnsets = RemoveIdenticalPoints(idLeft, idRight, diff);
		onsetsLeft = uniqueOnsets.arrayLeft;
		onsetsRight = uniqueOnsets.arrayRight;
		// RemoveIdentical prints => arrayLeft.size & arrayRight.size
		"onsetsLeft: ".post; onsetsLeft.postln;
		"onsetsRight: ".post; onsetsRight.postln;
		this.computeIOI(onsetsLeft, onsetsRight);
	}

	computeIOI { | ioiLeft, ioiRight |
		ioi = InterOnsetsPatch(ioiLeft, ioiRight);
		if( ( ioi.ioiLeftMean - ioi.ioiRightMean ).abs < threshold ) {
			"SUCCEED".postln;
			//this.makePlot(sndFilePath, )
		}{
			if ( ioi.ioiLeftMean > ioi.ioiRightMean ) {
				this.mergeOnsets( onsetsRight, 1 )
			}{
				this.mergeOnsets( onsetsLeft, 0 )
			};
		};
	}

	mergeOnsets { | array, which, diff=0.100 |
		var whichChannel;
		var currDiff = diff;
		counter = counter + 1;
		"How many times visits this method: ".post; counter.postln;
		currDiff = currDiff + ( counter/100 );
		"diff = ".post; currDiff.postln;

		whichChannel = MergeOnsetsPatch( array, currDiff );
		

		if( which == 0 ){
			this.computeIOI( whichChannel.outputArray, onsetsRight )
		}{
			this.computeIOI( onsetsLeft, whichChannel.outputArray )
		}
		
	}

	makePlot { | sndFilePath, take, onsetsLeft, onsetsRight |

		plotLeft = PlotOnsets(sndFilePath ++ take.asString ++".L.wav");
		plotRight = PlotOnsets(sndFilePath ++ take.asString ++".R.wav");

		plotLeft.plotOnsets(onsetsLeft, "set title \"Left Channel\"");
		plotRight.plotOnsets(onsetsRight, "set title \"Right Channel\"");
	}

}