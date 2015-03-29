MergeOnsets {
	var <>cnt = 0;
	var <>array, <>diff; // 50ms
	var <>mergeOnsets;
	var <>outputArray;

	*new { | array, diff, minimum |
		^super.new.init(array, diff, minimum)
	}

	init { | array, diff, minimum |
		this.wideWindow(array, diff)
	}

	wideWindow { | array, diff |
		var weights, item, arraySize;

		// mergeOnsets = [];
		mergeOnsets = array; //.round(0.001);
		arraySize = mergeOnsets.size;
		(arraySize - 1) do: { |i|
			cnt = cnt + 1;
			if ( arraySize > cnt ) {
				if ( (mergeOnsets[cnt][0] - mergeOnsets[cnt-1][0]) <= diff ) {
					weights = mergeOnsets[cnt-1][1] + mergeOnsets[cnt][1];
					item = ( mergeOnsets[cnt-1][0]*( mergeOnsets[cnt-1][1] / weights ) ) + ( mergeOnsets[cnt][0]*( mergeOnsets[cnt][1] / weights ) ).round(0.001);
					mergeOnsets.put(cnt-1, [item, weights]);
					mergeOnsets.removeAt(cnt);
					cnt = cnt - 1;
				};
			};
		};
		this.strictWindow(mergeOnsets, diff*2.15);
	}
	// the mergeOnset does not updated !! 
	strictWindow { | array, diff |
		var weights, item, arraySize;

		// mergeOnsets = [];
		mergeOnsets = array; //.round(0.001);
		arraySize = mergeOnsets.size;
		(arraySize - 1) do: { |i|
			cnt = cnt + 1;
			if ( arraySize > cnt ) {
				if ( (mergeOnsets[cnt][0] - mergeOnsets[cnt-1][0]) <= diff ) {
					weights = mergeOnsets[cnt-1][1] + mergeOnsets[cnt][1];
					item = ( mergeOnsets[cnt-1][0]*( mergeOnsets[cnt-1][1] / weights ) ) + ( mergeOnsets[cnt][0]*( mergeOnsets[cnt][1] / weights ) ).round(0.001);
					mergeOnsets.put(cnt-1, [item, weights]);
					mergeOnsets.removeAt(cnt);
					cnt = cnt - 1;
				};
			};
		};
	}

	removeOutliers { | minimum |
		var arr = [];
		mergeOnsets.size do: { |i|
			if ( mergeOnsets[i][1] > minimum ) {
				arr = arr ++ mergeOnsets[i];
			};
		};
		^outputArray = arr.clumps([2,2]);
	}
}
