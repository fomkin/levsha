var source = document.getElementById('source');
var result = document.getElementById('result');

function onInput() {

  var parser = new DOMParser();
  var doc = parser.parseFromString(source.value, "text/html");
  var walker = document.createTreeWalker(doc.body);

  function normalizeText(s) {
    var res = s.replace(/\n/g, ' ');
    while (res.indexOf('  ') > -1)
      res = res.replace('  ', ' ');
    return res;
  }

  function nodeToSymbolDsl(node, ident) {
    switch (node.nodeType) {
      case Node.ELEMENT_NODE:
        var s = "'" + node.tagName.toLowerCase() + "(\n";
        for (var i = 0; i < node.attributes.length; i++) {
          var attr = node.attributes[i];
          s += ident + "  '" + attr.name + ' /= "' + normalizeText(attr.value) + '"';
          if (i < node.attributes.length - 1 || node.childNodes.length > 0)
            s += ","
          s += "\n"
        }
        var prev = null;
        for (i = 0; i < node.childNodes.length; i++) {
          var childRes = nodeToSymbolDsl(node.childNodes[i], ident + "  ");
          if (childRes != null) {
            if (prev) s += ',\n  ' + ident + childRes
            else s += ident + '  ' + childRes
            prev = true;
          }
          if (i == (node.childNodes.length - 1))
            s += "\n"
        }
        s += ident + ")";
        return s;
      case Node.TEXT_NODE:
        var text = node.wholeText.trim();
        if (text.length > 0) {
          if (text.indexOf('\n') > -1) return '"""' + text + '"""';
          else return '"' + text + '"';
        } else {
          return null;
        }
      case Node.COMMENT_NODE:
        return '// ' + normalizeText(node.data);
    }
  }

  result.innerText = "";

  for (var i = 0; i < doc.body.childNodes.length; i++) {
    var res = nodeToSymbolDsl(doc.body.childNodes[i], "");
    if (res !== null) {
        if (i !== 0) result.innerText += ",\n";
      result.innerText += res;
    }
  }
}

source.addEventListener('input', onInput);
onInput()
