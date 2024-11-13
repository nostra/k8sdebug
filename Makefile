MAIN = build

all: build

clean:
	rm -rf site

site:
	mkdocs build

build: site
	echo \
	"html {" \
	"  zoom: 150%;" \
	"  -moz-transform: scale(1.5);" \
	"  -moz-transform-origin: 0 0;" \
	"}" >> site/css/theme_extra.css
	 echo "img {" \
        "float: right;" \
        "margin-left: 10px;" \
      "}" >> site/css/theme_extra.css






