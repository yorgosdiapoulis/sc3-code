ExtractOnsets {

var <>path, <>which;
var <>plotBuffer;
var <>pathName;
var <>onsets;
var <>loadAudioFile;
var <>normalizedOnsetsData;
var <>onsetsDataArray;
var <>startingFrame;
var <>metronomebpm;
var <win, <>view, <>sndFile;
var <file;

var <>playButton, <>stopButton;
var <soundfile, <synth, <synthFollower;
var <registerTime, <>oscrespoder;
var <>cursorFrame = 0.0;

	*new { | path, which |
		^super.new.init(path, which)
	}

	init { | path, which |
		this.onsetsFile(path, which);
	}

	onsetsFile { | path, which |
		loadAudioFile = SCMIRAudioFile(path);
		//
		loadAudioFile.extractOnsets(0.75, \rcomplex, 1, 0.55, 43, 59);

		onsets = loadAudioFile.onsetdata;
		// save ONSETS DATA to a file
		this.saveOnsetData(which);
		// FloatArray with the onsets in seconds
		"Onsets array = ".post; onsets.postln;
		onsets.class.postln;

		startingFrame = (onsets[0].round(0.000001) * Server.default.sampleRate).ceil.asInteger;
		"start frame: ".post; [startingFrame, startingFrame.class].postln;

		^this.plotting(path);
	}

	numOfOnsets {
		^("Numer of Onsets detected = ".post; onsets.size.postln)
	}

	plotting { | path |
		var i, tmpOnset;
		path.postln;

		this.loadPlaybackSynth(path);

		win = Window.new("test", Rect(200, 300, 1240, 380));
		view = SoundFileView.new(win, Rect(20, 60, 1200, 260));
		playButton = Button.new(win, Rect(50, 20, 250, 30))
		.states_([["Play Selection",Color.black, Color.new(0.76396951675415, 0.87935035228729, 0.62494311332703)]])
		.action_({
			soundfile.cue((bufferSize: sndFile.numFrames-startingFrame, addAction: 2, group: 1), playNow: true );
		});


		sndFile = SoundFile.new;
		sndFile.openRead(path);
		sndFile.inspect;

		view.soundfile = sndFile;
		view.read(startingFrame,  sndFile.numFrames-startingFrame);
		view.timeCursorOn = true;
		view.timeCursorColor = Color.white;
		view.timeCursorPosition = cursorFrame;
		view.refresh;
		// call this here to find the bpm of the track
		this.bpm;

		"onsets[0] = ".post; onsets[0].postln;
		"onsets class: ".post; onsets.class.postln;


		// create window
		win.front;

		// adjust vertical lines of original tempo assigned by the 4 first onsets of the metronome
		view.gridResolution = (metronomebpm/60).reciprocal.round(0.00001);

		// fix offset of vertical lines using the first value of the array
		view.gridOffset_(onsets[0].round(0.000001));
		view.refresh;

		"How many are the selections for SoundFileView: ".post; view.selections.size.postln;
		this.normalizeOnsets;

		onsets.size do: { |i|
			tmpOnset = (normalizedOnsetsData[i] * Server.default.sampleRate).ceil;
			"selection[".post; i.post; "]".post; " = ".post; tmpOnset.postln;

			if (i.even) {
				view.setSelectionColor(i, Color.red);
			}{
				view.setSelectionColor(i, Color.green);
			};
			// start each selection ? is this needed ?
			view.setSelection(i, [tmpOnset, tmpOnset+100]);

		};

		view.selections.postln;

		// FIX THIS
		win.onClose_({
			synth.release;
		});

	}

	normalizeOnsets {
		var i, tmp = [];

		onsets.size do: { |i|
			tmp = tmp ++ (onsets[i] - onsets[0])
		};

		normalizedOnsetsData = tmp;
		"normalizedOnsetsData: ".post; normalizedOnsetsData.class.postln;

		// .round() is used in order to transform FloatArray to Array
		onsetsDataArray = normalizedOnsetsData.round(0.000001);
		// return normalized array
		^normalizedOnsetsData
	}

	bpm {
		var metro = 0.0;
		var metroArray = [];
		var tmpMetro;
		var tmp = Array.newClear(4);

		5 do: { |k|
			metroArray = metroArray ++ onsets[k];
		};
		// metroArray = onsets.foldExtend(4);
		// find tempo
		4 do: { |i|
			tmp[i] = metroArray[i+1] - metroArray[i];
			metro = metro + tmp[i];
		};
		tmpMetro = metro/4;
		"Mean of Tempo: ".post; tmpMetro.postln;

		case
		{ tmpMetro < 0.45 } { metronomebpm = 150  }
		{ ((tmpMetro > 0.45)&&(tmpMetro < 0.58)) } { metronomebpm = 120 }
		{ ((tmpMetro > 0.58)&&(tmpMetro < 0.80)) } { metronomebpm = 90 }
		{ tmpMetro > 0.80 } { metronomebpm = 60 };

		^("metronome BPM: ".post; metronomebpm.postln;)
		//	^[tmp.mean, tmp]
		// ^metro
	}

	saveOnsetData { | which |
		if (which.notNil) {
			file = File.new("/Users/aucotsi/Desktop/" ++ which, "w");
			file.write(onsets.cs);
			file.close;
		}
	}

	loadPlaybackSynth { | path |
		Server.default.waitForBoot {
			synth = Synth(\diskInSoundFile);
			synthFollower = Synth(\onsetFollower2);

			oscrespoder = OSCFunc({ arg msg, time;
				[time, msg].postln;
				registerTime = time;
				"registerTime = ".post; registerTime.postln;
				//
				// time.postln;
				// view.timeCursorPosition = (time - registerTime).ceil;
				// ((time - registerTime)/Server.default.sampleRate);
				//
				{
					// FIX THIS TO PLAY THE LAST ONSET
					onsets.size do: { | i |
						if (i==0) { onsets[0].wait };
						Synth(\onsetTrigger);
						(onsets[i+1] - onsets[i]).wait;
					};

				}.fork;

				// {
				// 	var runningClk = 0;
				// 	loop {
				// 		//						cursorFrame = registerTime + runningClk;

				// 		cursorFrame = (runningClk * Server.default.sampleRate).ceil;
				// 		view.timeCursorPosition = cursorFrame;
				// 		view.refresh;

				// 		0.0001.wait;
				// 		"cursorFrame = ".post; cursorFrame.postln;
				// 		runningClk = runningClk + 0.0001;
				// 	}
				// }.fork(AppClock);

			},'/tr', Server.default.addr);

				// SystemClock.sched(0.0, { arg time;
				// 	time.postln;
				// 	onsets[3];
				// 	Synth(\onsetTrigger);
				// });

		};
		soundfile = SoundFile(path);
	}

}
/*

(
var myPath;
myPath = PathName.new("./");
myPath.files.postln;
)

*/

/*
	/// this is preloaded from your disk
(
SynthDef(\diskInSoundFile, { | bufnum, out, gate = 1, sustain, amp = 1, ar = 0, dr = 0.01 |
	Out.ar(out, DiskIn.ar(2, bufnum)
	* Linen.kr(gate, ar, 1, dr, 2)
	* EnvGen.kr(Env.linen(0, sustain - ar - dr max: 0 ,dr),1, doneAction: 2) * amp)
}).writeOnce;
)
	//
(
SynthDef(\onsetTrigger, { | freq=880 amp=0.8 |
	var sig, env;

	env = EnvGen.ar(Env.perc(0.001,0.1), doneAction:2);
	sig = SinOsc.ar(freq, 0, amp);

	Out.ar(0, sig!2 * env)

	}).wrireOnce;
)
Synth(\onsetTrigger);

	// this synth trig the input
s.boot;
//
(
SynthDef(\onsetFollower2, { | id=0 value |
	Out.ar(0, SendTrig.kr(A2K.kr(Amplitude.ar(In.ar(0))), id, 1))
}).add
)
(
// register to receive this message
o = OSCFunc({ arg msg, time;
	[time, msg].postln;
},'/tr', s.addr);
)
s.meter
Synth(\onsetFollower2)
().play

*/