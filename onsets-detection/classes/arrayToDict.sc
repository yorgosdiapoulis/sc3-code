+Array {
	arrayToDict {
		var array;
		var dict = IdentityDictionary.new;

		array = this;

		array.size do: { |i|
			dict.put( i.asSymbol, array[i] )
		};

		^dict

	}

}