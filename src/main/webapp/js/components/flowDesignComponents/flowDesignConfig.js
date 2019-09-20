var FlowDesignConfig = {
    taskNodes: [10, 20, 30],
    processNodes: [40],
    gatewayNodes: [50],
    taskNodeSorts: ['StartNoneEvent', 'UserTask', 'EndNoneEvent', 'EndTerminateEvent'],
    startNoneEventTypes: ['110', '210'],//开始节点节点类型
    componentNames: {
        'SubProcess': 'ElProcessNode',
        'UserTask': 'ElUserTask',
        'StartNoneEvent': 'ElStartNode',
        'ExclusiveGateway': 'ElExclusiveGateway',
        'EndNoneEvent': 'ElEndNoneEvent',
        'EndTerminateEvent': 'ElEndTerminateEvent',
        'SequenceFlow': 'ElSequenceFlow'
    }
}