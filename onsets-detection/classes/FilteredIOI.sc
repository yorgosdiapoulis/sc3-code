FilteredIOI {
	var <>array;
	var <>outputArray;

	*new { | array |
		^super.new.init(array)
	}

	init { | array |
		this.calculateIOI(array);
	}

	calculateIOI { | array |
		var ioiArray, tmpArray;
		var trash = List[];
		var tmp;
		var cnt = 0;
		var lolimit, hilimit, interval;

		ioiArray = array.findIOI;
		tmpArray = array;
		"ioiArray = ".post; ioiArray.postln;
		//ioiArray.asList.minIndex { |x| if( x < ( ioiArray.mean/2 ) ) { x.postln } };

		ioiArray.size do: { |i|
			if( ioiArray[i] < (ioiArray.mean * 0.75) ) {
				cnt = cnt + 1;
				">>>>> ctn <<<<<<".post; cnt.postln;
				trash.add(i);
				"TRASH: ".post; trash.postln;

			};
		};

		trash.size do: { |i|
			i.postln;
			tmp = trash[i];
			"tmp_i = ".post; tmp.postln;

			if( tmpArray[tmp].notNil ) {
				if( tmpArray[tmp][1] < tmpArray[tmp+1][1] ) {
					tmpArray[tmp] = nil;
				}{
					//				tmpArray[tmp+1] = nil;
					if( tmpArray[tmp][1] == tmpArray[tmp+1][1] ) {
						"<ERROR>".postln;

						interval = tmpArray[tmp][0] + tmpArray[tmp+1][0];
						lolimit = ioiArray.mean * 0.9;
						hilimit = ioiArray.mean * 1.1;

						if ( ( interval > lolimit ) && ( interval < hilimit ) ) {
							tmpArray[i+1] = nil;
						}{
							tmpArray[i] = nil;
						}

					}{
						tmpArray[tmp+1] = nil;
					}
				};
				"tmpArray = ".post; tmpArray.postln;
			}
		};
		"tmpArray=> = ".post; tmpArray.size.postln;
		tmpArray = tmpArray.removeNils;
		"tmpArray======>>>>>> = ".post; tmpArray.size.postln;
		outputArray = tmpArray;
		outputArray.postln;

	}

}
//
