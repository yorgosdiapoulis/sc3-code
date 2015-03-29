PopulatedData {
	var <file;
	var <iddict;
	var <tmpListElem;
	var <array;

	*new { | path, howMany |
		^super.new.init(path, howMany)
	}

	init { | path, howMany |
		this.readFiles(path, howMany);
	}

	readFiles { | path, howMany |
		var fileToArray;
		var weight = 0;
		var tmp;
		iddict = IdentityDictionary.new;
		tmpListElem = [];
		howMany do: { |i|
			file = File.new(path ++ (i+1).asString ++ ".txt", "rb");
			fileToArray = file.readAllString;
			// .findRegexp returns an array (1x2), in the form [onsets index in 'fileToArray', onset value]
			// the line below collects the 
			tmpListElem = fileToArray.findRegexp("[0-9]+\.[0-9]*") collect: { | index, i | index[1] };
			// this is collecting the elements in an IdentityDictionary in the form (\value, weight)
			tmpListElem.size do: { |i|
				tmp = tmpListElem[i].asSymbol;
				if ( iddict.includesKey( tmp ) ) {
					iddict[tmp] = iddict[tmp] + 1;
				}{
					iddict.put( tmp, 1 );
				};
			}; // end do
		};
		iddict.postln;
		this.makeArray;
	}

	makeArray {
		var tmpArray;
		tmpArray = iddict.getPairs.clumps([2,2]);
		array = tmpArray collect: { |index| index.asFloat.round(0.001) };
		array.sort { |x, y| x[0] < y[0] };
	}

}
//==========================================================\\
/*
(
a.iddict.sortedKeysValuesDo { | value, weight, i | 
	var oddValue, evenValue;
	value.asFloat.post; " - ".post; weight.postln; "i=".post; i.postln; 
	//
	if( i.odd ) { oddValue = value.asFloat } { evenValue = value.asFloat };
	if( ( oddValue - evenValue ) < 0.050 ) {
		
	}
}
)
*/
