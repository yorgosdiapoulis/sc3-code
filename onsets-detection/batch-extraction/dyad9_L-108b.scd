// BatchOnsets args: readPath, channel, savePath, threshold=0.5, relaxtime=1, floor=0.1, mingap=10, medianspan=11, floorTimes=1, threshTimes=1
MultipleOnsets("/Users/aucotsi/Desktop/mmt-yorgos/waveFiles/Marc_Dyad_09_", "L", "/Users/aucotsi/tmpData/dyad9_", relaxtime: 2, mingap: 40, medianspan: 60);

//
(
var floor=0.1, threshold=0.45, count=0;
			18 do: { |j|
				floor = floor + 0.05;

				12 do: { |k|
					threshold = threshold + 0.05;

					if ((threshold > floor)&&(threshold <= 0.9)) {
						count = count + 1;
						"floor: ".post; floor.postln;
						"threshold: ".post; threshold.postln;
					}
				};
				threshold = floor;
			};
			"count: ".post; count.postln;
)

/*
MultipleOnsets.sc
//
	readFile { | readPath, channel, savePath, threshold, relaxtime, floor, mingap, medianspan |
		24 do: { |i|
			if (i >= 12) {
			counter = i;
			audioFile = SCMIRAudioFile(readPath ++ (i+1).asString ++ "." ++ channel ++ ".wav");
			floor = 0.05; threshold = 0.40; count = 0;
			18 do: { |j|
				floor = floor + 0.05;
				12 do: { |k|
					threshold = threshold + 0.05;
					if ( (threshold > floor) && (threshold <= 0.90) ) {
						count = count + 1;
						this.featureExtraction(channel, savePath, threshold, relaxtime, floor, mingap, medianspan);
					};
				}; // end of |k| do
				threshold = floor;
			}; // end of |j| do
			}; // end of if
		}; // end of |i| do
	}

*/