SaveOnsets {
	var <file;

	*new { | arrayLeft, arrayRight, dyad, take, path  |
		^super.new.init( arrayLeft, arrayRight, dyad, take, path )
	}

	init { | arrayLeft, arrayRight, dyad, take, path |
		this.saveDocument( arrayLeft, arrayRight, dyad, take, path )
	}

	saveDocument { | arrayLeft, arrayRight, dyad, take, path |
		var left, right;
		left = arrayLeft.flatten.select{ | item, i | i.even };
		right = arrayRight.flatten.select{ | item, i | i.even };
		//
		file = File.new(path ++ "onsets_dyad_" ++ dyad.asString ++ "_" ++ take.asString ++ ".data", "wb");
		file.write(":L: " ++ left.cs ++ "\n:R: " ++ right.cs);
		file.close;
	}

}