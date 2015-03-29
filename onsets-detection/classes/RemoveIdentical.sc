RemoveIdentical {

	//var <>arrayL, <>arrayR;
	//var <>id, <>diff;
	var <>arrayLeft;
	var <>arrayRight;
	//var <>tmpElem;
	//var <>tmpList;

	*new { | arrayL, arrayR, id=0.050 |
		// id => time between identical onsets in the two arrays LR
		^super.new.init(arrayL, arrayR, id)
	}

	init { | arrayL, arrayR, id=0.050 |
		this.removeElements(arrayL, arrayR, id)
	}

	removeElements { | arrayL, arrayR, id=0.050 |
		var copyLeft = [];
		var copyRight = [];

		var tmp;
		var dict = IdentityDictionary.new;


		var currDiff;
		var counter = 0;
		// remove elements based on IOI (first ranking)
		// remove elements based on weights (second ranking)

		copyLeft = arrayL;
		copyRight = arrayR;

		arrayL.size do: { |i|
			arrayR.size do: { |j|
				currDiff = ( copyLeft[i][0] - copyRight[j][0] ).abs;
				if ( currDiff < id ) {
					dict.put( counter.asSymbol, [i, j] );
					counter = counter + 1;
				};
			};
		};

		"tmpDict: ".post; dict.postln;

		dict.size do: { |k|
			tmp = dict.at( k.asSymbol );
			"tmp: ".post; tmp.postln;

			case
			{ copyLeft[ tmp[0] ][ 1 ] < copyRight[ tmp[1] ][ 1 ] }{
				copyLeft[ tmp[0] ] = nil;
			}
			{ copyLeft[ tmp[0] ][ 1 ] > copyRight[ tmp[1] ][ 1 ] }{
				copyRight[ tmp[1] ] = nil;
			};

		};

		arrayLeft = copyLeft.removeNils;
		"arrayLeft.size = ".post; arrayLeft.size.postln;
		//
		arrayRight = copyRight.removeNils;
		"arrayRight.size = ".post; arrayRight.size.postln;

	}

}
