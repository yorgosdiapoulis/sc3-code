MultipleOnsets {

var <>readPath;
var <>savePath;
var <>audioFile;
var <>onsets;
var <>threshold, <>floor;
var <>counter = 0; // this is the "incremental" number of the take
var <>count = 1;
var <>file;

	*new { | readPath, channel, savePath, threshold=0.5, relaxtime=1, floor=0.1, mingap=10, medianspan=11 |
		^super.new.init(readPath, channel, savePath, threshold, relaxtime, floor, mingap, medianspan)
	}

	init {  | readPath, channel, savePath, threshold=0.5, relaxtime=1, floor=0.1, mingap=10, medianspan=11 |
		this.readFile(readPath, channel, savePath, threshold, relaxtime, floor, mingap, medianspan)
	}

	readFile { | readPath, channel, savePath, threshold, relaxtime, floor, mingap, medianspan |
		24 do: { |i|
			counter = i;
			audioFile = SCMIRAudioFile(readPath ++ (i+1).asString ++ "." ++ channel ++ ".wav");
			floor = 0.25; threshold = 0.35; count = 0;
			10 do: { |j|
				floor = floor + 0.05;
				12 do: { |k|
					threshold = threshold + 0.05;
					if ( (threshold > floor) && (threshold <= 1.0) ) {
						count = count + 1;
						this.featureExtraction(channel, savePath, threshold, relaxtime, floor, mingap, medianspan);
					};
				}; // end of 12 do
				threshold = floor;
			}; // end of 10 do
		}
	}

	featureExtraction { | channel, savePath, threshold, relaxtime, floor, mingap, medianspan |
		audioFile.extractOnsets(threshold, \rcomplex, relaxtime, floor, mingap, medianspan);
		this.saveOnsetData(channel, savePath, threshold, relaxtime, floor, mingap, medianspan);
	}

	saveOnsetData { | channel, savePath, threshold, relaxtime, floor, mingap, medianspan |
		onsets = audioFile.onsetdata;
		file = File.new(savePath ++ channel ++ "." ++ (counter+1).cs ++ "-" ++ count.cs ++ ".txt" , "wb");
		file.write(onsets.cs);
		file.close;
	}

}

/* RUN THIS
MultipleOnsets("/Users/aucotsi/Desktop/mmt-yorgos/waveFiles/Marc_Dyad_07_", "R", "/Users/aucotsi/Desktop/onsetsDataMulti/dyad7_");
*/