var SVGNODE = {
    taskNodes: [{
        type: 'g',
        translate: 'matrix(1,0,0,1,0,0)',
        rotate: 'matrix(1,0,0,1,0,0)',
        scale: 'matrix(0.5,0,0,0.5,0,0)',
        children: [{
            type: 'circle',
            translate: 'matrix(1,0,0,1,60,60)',
            attr: {
                r: 60,
                fill: '#ffcc99',
                style: 'stroke-width: 1px; stroke: #ffcc99; fill-opacity: 1'
            }
        }, {
            type: 'text',
            text: '开始',
            translate: 'matrix(1,0,0,1,30,35)',
            attr: {
                fill: "#ffffff",
                style: 'font-size: 12px; font-family: "Microsoft Yahei"; text-anchor: middle;'
            }
        }]
    }, {
        type: 'g',
        translate: 'matrix(1,0,0,1,70,0)',
        rotate: 'matrix(1,0,0,1,0,0)',
        scale: 'matrix(0.5,0,0,0.5,0,0)',
        children: [{
            type: 'circle',
            translate: 'matrix(1,0,0,1,60,60)',
            attr: {
                r: 60,
                fill: '#ffcc99',
                style: 'stroke-width: 1px; stroke: #ffcc99; fill-opacity: 1'
            }
        }, {
            type: 'text',
            text: '结束',
            translate: 'matrix(1,0,0,1,30,35)',
            attr: {
                fill: "#ffffff",
                style: 'font-size: 12px; font-family: "Microsoft Yahei"; text-anchor: middle;'
            }
        }]
    }, {
        type: 'g',
        nodeType: 'baseNode',
        translate: 'matrix(1,0,0,1,1,75)',
        rotate: 'matrix(1,0,0,1,0,0)',
        scale: 'matrix(0.5,0,0,0.5,0,0)',
        title: {
            type: 'g',
            translate: 'matrix(1,0,0,1,0,1)',
            scale: 'matrix(0.5,0,0,0.5,0,0)',
            children: [{
                type: 'rect',
                translate: 'matrix(1,0,0,1,0,0)',
                attr: {
                    width: 256,
                    height: 60,
                    rx: 3,
                    ry: 3,
                    fill: '#ffcc99',
                    style: 'stroke-width: 1px; stroke: #ffcc99; fill-opacity: 1; border-radius: 3px;'
                }
            }, {
                type: 'text',
                text: '任务名称',
                translate: 'matrix(1,0,0,1,65,20)',
                attr: {
                    fill: "#ffffff",
                    style: 'font-size: 12px; font-family: "Microsoft Yahei"; text-anchor: middle;'
                }
            }]
        },
        auditRoles: {
            type: 'g',
            translate: 'matrix(1,0,0,1,1,30)',
            roles: [{
                type: 'text',
                text: '选择角色',
                translate: 'matrix(1,0,0,1,62,20)',
                attr: {
                    fill: "#333",
                    style: 'font-size: 12px; font-family: "Microsoft Yahei"; text-anchor: start;'
                }
            }],
            children: [{
                type: 'text',
                text: '审核角色：',
                translate: 'matrix(1,0,0,1,2,20)',
                attr: {
                    fill: "#333",
                    style: 'font-size: 12px; font-family: "Microsoft Yahei"; text-anchor: start;'
                }
            }]
        },
        auditUsers: {
            type: 'g',
            translate: 'matrix(1,0,0,1,1,50)',
            users: [{
                type: 'text',
                text: '选择审核人',
                translate: 'matrix(1,0,0,1,62,20)',
                attr: {
                    fill: "#333",
                    style: 'font-size: 12px; font-family: "Microsoft Yahei"; text-anchor: start;'
                }
            }],
            children: [{
                type: 'text',
                text: '审核人：',
                translate: 'matrix(1,0,0,1,9,20)',
                attr: {
                    fill: "#333",
                    style: 'font-size: 12px; font-family: "Microsoft Yahei"; text-anchor: start;'
                }
            }]
        },
        children: [{
            type: 'rect',
            attr: {
                width: 256,
                height: 180,
                rx: 3,
                ry: 3,
                translate: 'matrix(1,0,0,1,0,0)',
                fill: 'transparent',
                style: 'stroke-width: 1px; stroke: #ffcc99; fill-opacity: 1;'
            }
        }]
    }],
    processNodes: [{
        type: 'g',
        nodeType: 'subNodeGroup',
        translate: 'matrix(1,0,0,1,1,1)',
        rotate: 'matrix(1,0,0,1,0,0)',
        scale: 'matrix(0.5,0,0,0.5,0,0)',
        title: {
            type: 'g',
            translate: 'matrix(1,0,0,1,0,-1)',
            scale: 'matrix(0.5,0,0,0.5,0,0)',
            children: [{
                type: 'rect',
                translate: 'matrix(1,0,0,1,0,0)',
                attr: {
                    rx: 3,
                    ry: 3,
                    width: 256,
                    height: 60,
                    fill: '#ffcc99',
                    style: 'stroke-width: 1px; stroke: #ffcc99; fill-opacity: 1; border-radius: 3px;'
                }
            }, {
                type: 'text',
                text: '子流程名称',
                translate: 'matrix(1,0,0,1,65,20)',
                attr: {
                    fill: "#ffffff",
                    style: 'font-size: 12px; font-family: "Microsoft Yahei"; text-anchor: middle;'
                }
            }]
        },
        children: [{
            type: 'rect',
            translate: 'matrix(1,0,0,1,0,0)',
            attr: {
                width: 256,
                height: 200,
                rx: 3,
                ry: 3,
                fill: 'transparent',
                style: 'stroke-width: 1px; stroke: #ccc; fill-opacity: 1;'
            }
        }]
    }],
    gatewayNodes: [{
        type: 'g',
        translate: 'matrix(1,0,0,1,1,1)',
        scale: 'matrix(0.5,0,0,0.5,1,1)',
        rotate: 'matrix(1,0,0,1,1,1)',
        nodeType: 'gateway',
        children: [{
            type: 'polygon',
            translate: 'matrix(1,0,0,1,1,1)',
            attr: {
                points: '100,0 200,100 100,200 0,100',
                fill: 'transparent',
                style: 'stroke-width: 1px; stroke: #ffcc99'
            }
        }, {
            type: 'text',
            text: '网关',
            translate: 'matrix(1,0,0,1,55,55)',
            attr: {
                fill: "#000",
                style: 'font-size: 12px; font-family: "Microsoft Yahei"; text-anchor: middle;'
            }
        }]
    }]
};