+Array { // [a,b] * [[x,y], [v,w]]
	ioiInnerProduct { | args |
		var x, y, outputX, outputY, output;

		x = this.value;
		y = args;
		outputX = ( x[0] * y[0][0] ) + ( x[1] * y[1][0] );
		outputY = ( x[0] * y[0][1] ) + ( x[1] * y[1][1] );

		^output = [ outputX, outputY ];
		//^[this.value, args]
	}

}
//[1,2].ioiInnerProduct( [[3,2],[2,3]] )