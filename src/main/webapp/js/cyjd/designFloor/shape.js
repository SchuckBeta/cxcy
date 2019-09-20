var BASICSHAPE = [
    {
        "type": "room",
        "shapeType": "rect",
        "transform": "matrix(1,0,0,1,3,1)",
        "roomId": "",
        name: 'test',
        "rotate": 'rotate(0, 0 , 0)',
        'angle': '0',
        "attr": {
            "fill": "transparent",
            "x": "0",
            "y": "0",
            "width": "120",
            "height": "100",
            "style": "stroke-width: 1px;stroke: #222138"
        },
        "children": [
            {
                "type": "roomAsset",
                "shapeType": "text",
                "text": "房间名",
                name: 'test',
                "roomId": "",
                "attr": {
                    "x": "60",
                    "y": "45",
                    "fill": "#333",
                    "style": "font-size: 12px;font-family:Arial;text-anchor:middle;"
                }
            }
        ]
    }
]

var ASSETS = [{
    type: 'roomAsset',
    shapeType: 'text',
    name: '描述文字',
    text: '描述\n文字',
    attr: {
        "x": 15,
        "y": 0,
        "fill": "#e9442d",
        style: 'font-size: 12px;font-family:Arial;text-anchor:middle;'
    }
}, {
    type: 'roomAsset',
    shapeType: 'image',
    name: '单人办公桌',
    attr: {
        fill: 'transparent',
        x: 40,
        y: 0,
        width: 30,
        height: 30,
        style: 'stroke-width: 0',
        href: '/images/roomAsset/desk1.png'
    }
}, {
    type: 'roomAsset',
    shapeType: 'image',
    name: '双人办公桌',
    attr: {
        fill: 'transparent',
        x: 80,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/roomAsset/desk2.png'
    }
}, {
    type: 'roomAsset',
    shapeType: 'image',
    name: '4人办公桌',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 40,
        width: 30,
        height: 30,
        style: '',
        href: '/images/roomAsset/desk4.png'
    }
}, {
    type: 'roomAsset',
    shapeType: 'image',
    name: '10人办公桌',
    attr: {
        fill: 'transparent',
        x: 40,
        y: 40,
        width: 30,
        height: 30,
        style: '',
        href: '/images/roomAsset/desk10.png'
    }
}, {
    type: 'roomAsset',
    shapeType: 'image',
    name: '1人沙发',
    attr: {
        fill: 'transparent',
        x: 80,
        y: 40,
        width: 30,
        height: 30,
        style: '',
        href: '/images/roomAsset/sofa1.png'
    }
}, {
    type: 'roomAsset',
    shapeType: 'image',
    name: '3人沙发',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 80,
        width: 30,
        height: 30,
        style: '',
        href: '/images/roomAsset/sofa3.png'
    }
}, {
    type: 'roomAsset',
    shapeType: 'rect',
    name: '文字背景',
    attr: {
        fill: '#ffffff',
        x: 80,
        y: 80,
        width: 30,
        height: 30,
        style: 'stroke-width: 2px; stroke: #e9442d'
    }
}]

var TRIMMING = [{
    "type": "trimming",
    "shapeType": "rect",
    "transform": "matrix(1,0,0,1,3,1)",
    name: 'test',
    "rotate": 'rotate(0, 0 , 0)',
    'angle': '0',
    "attr": {
        "fill": "transparent",
        "x": "1",
        "y": "1",
        "width": "120",
        "height": "50",
        "style": "stroke-width: 1px;stroke: #222138"
    },
    "children": [
        {
            "type": "roomAsset",
            "shapeType": "text",
            name: '描述文字',
            text: '描述文字',
            "roomId": "",
            "attr": {
                "x": "60",
                "y": "15",
                "fill": "#333",
                "style": "font-size: 12px;font-family:Arial;text-anchor:middle;"
            }
        }
    ]
}, {
    type: 'trimming',
    shapeType: 'image',
    transform: 'matrix(1,0,0,1,1,60)',
    name: '双门',
    'angle': '0',
    "rotate": 'rotate(0,0,0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/doubeldoor.png'
    },
    nameAttr: {
        x: 0,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
}, {
    type: 'trimming',
    shapeType: 'image',
    name: '办公桌',
    transform: 'matrix(1,0,0,1,50,60)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 1,
        y: 1,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/workTable2.png'
    },
    nameAttr: {
        x: 10,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
}, {
    type: 'trimming',
    shapeType: 'image',
    name: '单门',
    transform: 'matrix(1,0,0,1,100,60)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/door.png'
    },
    nameAttr: {
        x: 10,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
}, {
    type: 'trimming',
    shapeType: 'image',
    name: '窗户',
    transform: 'matrix(1,0,0,1,1,120)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/window.png'
    },
    nameAttr: {
        x: 10,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
}, {
    type: 'trimming',
    shapeType: 'image',
    name: '会议桌',
    transform: 'matrix(1,0,0,1,50,120)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/circleTable.png'
    },
    nameAttr: {
        x: 0,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
}, {
    type: 'trimming',
    shapeType: 'image',
    name: '会议桌',
    transform: 'matrix(1,0,0,1,100,120)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/meetingTableBig.png'
    },
    nameAttr: {
        x: 0,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
}, {
    type: 'trimming',
    shapeType: 'image',
    name: '会议桌',
    transform: 'matrix(1,0,0,1,1,180)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/rectMeetingTable.png'
    },
    nameAttr: {
        x: 0,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
},{
    type: 'trimming',
    shapeType: 'image',
    name: '椅子',
    transform: 'matrix(1,0,0,1,50,180)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/chair.png'
    },
    nameAttr: {
        x: 0,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
},{
    type: 'trimming',
    shapeType: 'image',
    name: '沙发',
    transform: 'matrix(1,0,0,1,100,180)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/oneSofa.png'
    },
    nameAttr: {
        x: 0,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
},{
    type: 'trimming',
    shapeType: 'image',
    name: '双人沙发',
    transform: 'matrix(1,0,0,1,1,240)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/twoSofa.png'
    },
    nameAttr: {
        x: 0,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
},{
    type: 'trimming',
    shapeType: 'image',
    name: '三人沙发',
    transform: 'matrix(1,0,0,1,50,240)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/threeSofa.png'
    },
    nameAttr: {
        x: 0,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
},{
    type: 'trimming',
    shapeType: 'image',
    name: '四人沙发',
    transform: 'matrix(1,0,0,1,100,240)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/fourSofa.png'
    },
    nameAttr: {
        x: 0,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
},{
    type: 'trimming',
    shapeType: 'image',
    name: '会议桌',
    transform: 'matrix(1,0,0,1,1,300)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/fourMeetingTable.png'
    },
    nameAttr: {
        x: 0,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
},{
    type: 'trimming',
    shapeType: 'image',
    name: '会议桌',
    transform: 'matrix(1,0,0,1,50,300)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/rectEightTable.png'
    },
    nameAttr: {
        x: 0,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
},{
    type: 'trimming',
    shapeType: 'image',
    name: '窗户',
    transform: 'matrix(1,0,0,1,100,300)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/window2.png'
    },
    nameAttr: {
        x: 0,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
},{
    type: 'trimming',
    shapeType: 'image',
    name: '盆栽',
    transform: 'matrix(1,0,0,1,1,360)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/basin.png'
    },
    nameAttr: {
        x: 0,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
},{
    type: 'trimming',
    shapeType: 'image',
    name: '办公桌',
    transform: 'matrix(1,0,0,1,50,360)',
    'angle': '0',
    "rotate": 'rotate(0, 0 , 0)',
    attr: {
        fill: 'transparent',
        x: 0,
        y: 0,
        width: 30,
        height: 30,
        style: '',
        href: '/images/trimming/workTable.png'
    },
    nameAttr: {
        x: 0,
        y: 35,
        fill: '#333',
        style: 'font-size: 10px;font-family:Arial;'
    }
}]