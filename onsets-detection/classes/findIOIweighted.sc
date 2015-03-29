+Array {
	findIOIweighted {
		var array = this;
		var tmpValue, tmpWeight;
		var newArray, tmp;
		tmp = array.size-1;
		newArray = Array.newClear(tmp);

		tmp do: { |i|
			tmpValue = array[i+1][0] - array[i][0];
			tmpWeight = array[i+1][1] - array[i][1];
			newArray[i] = [tmpValue, tmpWeight];
		}
		^newArray
	}
}
//
