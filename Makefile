clean:
	rm -rf target/lib/native

libdrafter:
	cd ext/drafter; ./configure --shared
	$(MAKE) -C ext/drafter clean libdrafter
	mkdir -p target/classes
	cp ext/drafter/build/out/Release/lib.target/libdrafter.so target/classes/libdrafter.so
