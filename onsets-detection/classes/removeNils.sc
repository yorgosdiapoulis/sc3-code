+Array {
	removeNils {
		var array;
		array = this.value;
		if ( array.rank == 1 ) {
			array = this.select { |x| x.notNil };
		}{
			array = this.flatten.select { |x| x.notNil };
			array = array.clumps([2,2])
		}
		^array
	}
}
//
/*a=[[2,3],[5,6]]
a.rank*/
//
