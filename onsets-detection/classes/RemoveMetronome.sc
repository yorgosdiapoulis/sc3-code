RemoveMetronome {

	var <>arrayL, <>arrayR;
	var <>arrayLeft, <>arrayRight;
	var <cntL=0, <cntR=0, <posL=0, <posR=0;
//	var <>id; //20ms
//	var <>diff; // this is the dfferent between the weights in L and R channel

	*new { | arrayL, arrayR, id=0.050, diff=20 |
		^super.new.init( arrayL, arrayR, id, diff );
	}

	init { | arrayL, arrayR, id=0.050, diff=20 |
		this.removeElements(arrayL, arrayR, id, diff )
	}

	removeElements { | arrayL, arrayR, id=0.050, diff=20 |
		var arrLeft = [];
		var arrRight = [];
		var copyLeft = [];
		var copyRight = [];
		var elemL = 0;
		var elemR = 0;
		// i, j should be assigned to ~i, ~j ?
		( arrayL.size - 1 ) do: { |i|
			( arrayR.size - 2 ) do: { |j|

				if ( ( abs(arrayL[i][0] - arrayR[j][0]) < id ) && ( abs( arrayL[i][1] - arrayR[j][1] ) < diff ) ) {
					arrLeft = arrLeft ++ i;
					arrRight = arrRight ++ j;
					"i: ".post; i.postln;
					"j: ".post; j.postln;
				};
			}; // end of arrayR
		}; //end of arrayL

		arrRight.postln;
		arrLeft.postln;
		// store true, false values for IOI
		( arrLeft.size - 1 ) do: { |i|
			if ( (arrLeft[i+1] - arrLeft[i]) == 1 ) {
				copyLeft = copyLeft ++ true;
			}{
				copyLeft = copyLeft ++ false;
			};
		};
		"copyLeft".post; copyLeft.postln;
		( arrRight.size - 1 ) do: { |i|
			if ( (arrRight[i+1] - arrLeft[i]) == 1 ) {
				copyRight = copyRight ++ true;
			}{
				copyRight = copyRight ++ false;
			};
		};
		"copyRight".post; copyRight.postln;
		//
		arrayRight = arrayR;
		"arrayRight".post; arrayRight.postln;
		arrayLeft = arrayL;
		"arrayLeft".post; arrayLeft.postln;
		// the loops work only if arrayRL[0] is on both channel
		// it should be generalized to ignore the first element of the array
		// and look for a sequence of true's
		( copyLeft.size - 1 ) do: { |i|
			if ( copyLeft[i] == true ) {
				cntL = cntL + 1;
				posL = i; // I am not sure yet about this?
					// should be like pos - cnt ?
				if (copyLeft.size == 7) {
					(copyLeft.size + 1) do: { arrayLeft.removeAt(0) }
				}
			}{
				( cntL + 1 ) do: { arrayLeft.removeAt(0) };
				cntL = 0;
			};
		};
		"\n\n".post;
		"arrayLeft".post; arrayLeft.postln;
		//
		( copyRight.size - 1 ) do: { |i|
			if ( copyRight[i] == true ) {
				cntR = cntR + 1;
				posR = i; // I am not sure yet about this?
				//
				if (copyRight.size == 7) {
					(copyRight.size + 1) do: { arrayRight.removeAt(0) }
				}
			}{
				( cntR + 1 ) do: { arrayRight.removeAt(0) };
				cntR = 0;
			};
		};
		"arrayRight".post; arrayRight.postln;
	}

}
