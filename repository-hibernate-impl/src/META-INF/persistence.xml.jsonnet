local xml = import "../../../tools/jsonnet/xml.libsonnet";

local Svg = xml.Element('svg') {
  height:: 100,
  width:: 100,
};

local Polygon = xml.Element('polygon') {
  points: std.join(
    ' ',
    [
      '%s,%s' % coords
      for coords in self.pointCoords
    ]
  ),
};

local Path = xml.Element('path') {
  cmds:: [],
  d: std.join('', self.cmds),
};

local ref(element) = 'url(#%s)' % element.id;

local logo = Svg {
  version: '1.1',
  xmlns: 'http://www.w3.org/2000/svg',
  x: 0,
  y: 0,
  width: 90,
  height: 90,
  viewBox: '0 0 180 180',
  bg:: xml.Element('linearGradient') {
    id: 'bg',
    gradientUnits: 'userSpaceOnUse',
    x1: 53,
    y1: 19,
    x2: 126,
    y2: 162,
    colour1:: '#0091AD',
    colour2:: '#00728F',
    local grad = self,
    has: [
      xml.Element('stop') {
        offset: 0,
        'stop-color': grad.colour1,
      },
      xml.Element('stop') {
        offset: 1,
        'stop-color': grad.colour2,
      },
    ],
  },
  line:: Polygon {
    fill: 'white',
    pointCoords:: [
      [0, 38],
      [90, -14],
      [180, 38],
      [180, 142],
      [90, 194],
      [0, 142]
    ],
  },
  fill:: Path {
    fill: ref($.bg),
    cmds: [
      'M168,49 v86 L129,82 L168,49 z',
      'M90,98 l67-59 L90,0 V98 z',
      'M73,10 L12,45 v90 l61,35 V10 z',
      'M117,94 L90,116 v63 l63-36 L117,94 z',
    ],
  },
  has: [
    $.bg,
    $.line,
    $.fill,
  ],
};

xml.manifestXmlObj(logo)