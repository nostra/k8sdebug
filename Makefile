MAIN = build

all: build

clean:
	rm -rf site

build:
	mkdocs build