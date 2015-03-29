// ^super.new.init(readPath, channel, savePath, threshold, relaxtime, floor, mingap, medianspan)
MultipleOnsets("/Users/aucotsi/Desktop/mmt-yorgos/waveFiles/Marc_Dyad_09_", "R", "/Users/aucotsi/tmpData/dyad9_", relaxtime: 2, mingap: 43, medianspan: 59);
//
(
var floor=0.25, threshold=0.35, count=0;
			20 do: { |j|
				floor = floor + 0.05;

				20 do: { |k|
					threshold = threshold + 0.05;

					if ((threshold > floor)&&(threshold <= 0.90)) {
						count = count + 1;
						"floor: ".post; floor.postln;
						"threshold: ".post; threshold.postln;
					}
				};
				threshold = floor;
			};
			"count: ".post; count.postln;
)

ExtractOnsetsMulti