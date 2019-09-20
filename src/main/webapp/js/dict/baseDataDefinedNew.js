(function (name, definition) {
    var hasDefine = typeof define === 'function';
    var hasExports = typeof module !== 'undefined' && module.exports;
    if (hasDefine) {
        define(definition)
    } else if (hasExports) {
        module.exports = definition()
    } else {
        this[name] = definition()
    }
})('baseData', function () {
    var baseData;
    baseData = new Vue({
        el: '#baseData',
        data: function () {
            return {
                projectTypes: [],
                ptTypes: [],
                modalAddEdit: {
                    title: '',
                    show: false
                },
                modalDelete: {
                    title: '',
                    show: false
                },
                modalAdd: {
                    show: false
                },
                // dismissCountDown: 0,
                showDismissibleAlert: 0,
                ptTypeVal: 0,
                metaKey: null,
                ptTypeName: '',
                menuCollaspe: true,
                delText: '',
                currentPage: 1,
                pageSize: 10,
                total: 0,
                tablePage: true,
                dictjson: [],
                currentDictIndex: -1,
                dictItem: {
                    typeid: '',
                    name: ''
                },
                dictName: '',
                edtDictTypeAble: false,
                modalChangeDictType: {
                    form: {
                        name: '',
                        id: '',
                        sort: ''
                    },
                    show: false
                },
                modalChangeDict: {
                    form: {
                        name: '',
                        id: '',
                        sort: '',
                        pId: ''
                    }
                },
                modalChangeDictTypeTitle: '修改字典类型',
                delItem: {},
                alertTitle: '',
                variantType: 'success',
                changeType: 'edtDict',
                dictTimerId: null,
                dictTimerId2: null,
                dictTimerId3: null,
                filterDicts: [],
                dTypeId: 1,
                dictNameShow: false,
                filterDictsAdd: [],
                dictFormName: '',
                dictNamePlaceholder: '',
                dataLoad: false
            }
        },
        watch: {
            dictName: function (value, oldValue) {
                var self = this;
                (this.dictTimerId || value == oldValue) && clearTimeout(this.dictTimerId);
                this.dictTimerId = setTimeout(function () {
                    var dictJsonItem;
                    var dcitJsonIndex;
                    self.filterDicts = self.dictjson.filter(function (item, i) {
                        return item.label.indexOf(value) > -1
                    });
                    // if(self.filterDicts.length == 1){
                    //     self.changeDictJson(dictJsonItem, dcitJsonIndex);
                    // }
                    // self.dictjson.forEach(function (item, i) {
                    //     if (item.label.indexOf(value)) {
                    //         self.changeDictJson(item, i);
                    //     }
                    // })
                    // self.currentDictIndex = '';
                }, 300)
            },
            dictFormName: function (value, oldValue) {
                var self = this;
                (this.dictTimerId2 || value == oldValue) && clearTimeout(this.dictTimerId2);
                this.dictTimerId2 = setTimeout(function () {
                    self.filterDictsAdd = self.dictjson.filter(function (item) {
                        return item.label.indexOf(value) > -1;
                    });
                    if (self.filterDictsAdd.length > 10) {
                        self.filterDictsAdd.splice(10)
                    }
                }, 300)
            },
            metaKey: function (value, oldValue) {
                var self = this;
                (this.dictTimerId3 || value == oldValue) && clearTimeout(this.dictTimerId3);
                this.dictItem.name = value;
                this.dictTimerId3 = setTimeout(function () {
                    self.formSearch();
                }, 300)
            }
        },
        methods: {
            addDictFocus: function () {
                this.dictNameShow = true;
                if (this.filterDictsAdd.length > 10) {
                    this.filterDictsAdd.splice(10)
                }
            },
            addDictBlur: function () {
                var self = this;
                setTimeout(function () {
                    self.dictNameShow = false;
                }, 300)

            },
            dictFormChange: function () {
                var self = this;
                this.dictjson.forEach(function (item) {
                    self.dTypeId = item.label.indexOf(self.dictFormName) > -1;
                });
            },

            selectDict: function (item) {
                this.dTypeId = item.id;
                this.dictFormName = item.label;
                this.dictNameShow = false;
            },

            formCancel: function () {
                this.dictItem.typeid = '';
                this.dictItem.name = '';
                this.currentDictIndex = -1;
                this.metaKey = '';
                this.currentPage = 1;
                this.getDictPagePlus();
            },
            ptTagChange: function (event) {
                if (this.modalChangeDict.form.name) {
                    $(event.target).next().remove()
                }
            },
            pTypeChange: function () {
                if (this.modalChangeDict.form.id) {
                    $(event.target).next().remove()
                }
            },
            sortChange: function () {
                if ((/^[0-9]*$/.test(this.modalChangeDict.form.sort))) {
                    $(event.target).next().remove()
                }
            },
            editPtType: function (currIndex, row) {
                this.modalAddEdit.title = '编辑';
                this.modalAddEdit.show = true;
                this.changeType = 'edtDict';
                this.modalChangeDict.form = {
                    id: row.id,
                    name: row.label,
                    sort: row.sort,
                    pId: row.parent_id
                }
            },
            deletePtType: function (currIndex, row) {
                this.delText = '确认要删除这条字典吗？';
                this.modalDelete.title = "删除";
                this.modalDelete.show = true;
                this.changeType = 'delDictType';
                this.delItem.id = row.id;

            },
            modalAddEditOk: function () {
                if (!this.modalChangeDict.form.id) {
                    if ($('#ptType1').next().size() < 1) {
                        $('#ptType1').after('<label class="error" style="color:red">请选择一个字典类型</label>');
                    }
                    return;
                }
                if (!this.modalChangeDict.form.name) {
                    if ($('#ptTypeName').next().size() < 1) {
                        $('#ptTypeName').after('<label class="error" style="color:red">请填写字典名称</label>');
                    }
                    return;
                }
                if (typeof this.modalChangeDict.form.sort != 'undefined' && !(/^[0-9]*$/.test(this.modalChangeDict.form.sort))) {
                    if ($('#ptTypeSort').next().size() < 1) {
                        $('#ptTypeSort').after('<label class="error" style="color:red">请填写数字</label>');
                    }
                    return;
                }
                var self = this;
                var xhr = $.post('/a/sys/dict/edtDict', {
                    name: this.modalChangeDict.form.name,
                    id: this.modalChangeDict.form.id,
                    sort: this.modalChangeDict.form.sort
                });
                xhr.success(function (data) {
                    if (data.ret == '1') {
                        self.variantType = 'success';
                        self.getDictPagePlus();
                    } else {
                        self.variantType = 'danger';
                    }
                    self.modalChangeDict.form = {
                        pId: '',
                        id: '',
                        sort: '',
                        name: ''
                    };
                    self.alertTitle = data.msg;
                    self.showDismissibleAlert = 2;
                    // self.dismissCountDown = 1
                });
                this.modalAddEdit.show = false;
                this.modalChangeDict.form = {
                    pId: '',
                    id: '',
                    sort: '',
                    name: ''
                }
            },

            modalAddEditCancel: function () {
                this.modalChangeDict.form = {
                    name: '',
                    id: '',
                    sort: '',
                    pId: ''
                };
                this.modalAddEdit.show = false;
                $('.error').remove();
            },
            formAdd: function () {
                var addName, addId;
                var self = this;
                this.modalAdd.show = true;
                this.changeType = 'addDict';
                if (this.currentDictIndex > -1) {
                    this.filterDicts.forEach(function (item, i) {
                        if (i === self.currentDictIndex) {
                            addName = item.label;
                            addId = item.id
                        }
                    });
                    this.dictFormName = addName;
                    this.dTypeId = addId;
                }

            },

            modalAddOk: function () {
                if (!this.dTypeId || this.dTypeId == 1) {
                    this.dTypeId = false;
                    return false;
                }
                if (!this.modalChangeDict.form.name) {
                    if ($('#ptTag').next().size() < 1) {
                        $('#ptTag').after('<label class="error" style="color:red">请填写字典名称</label>');
                    }
                    return false;
                }
                if (typeof this.modalChangeDict.form.sort != 'undefined' && !(/^[0-9]*$/.test(this.modalChangeDict.form.sort))) {
                    if ($('#ptDesc').next().size() < 1) {
                        $('#ptDesc').after('<label class="error" style="color:red">请填写数字</label>');
                    }
                    return false;
                }
                var self = this;
                var xhr = $.post('/a/sys/dict/addDict', {
                    name: this.modalChangeDict.form.name,
                    typeid: this.dTypeId,
                    sort: this.modalChangeDict.form.sort
                });
                xhr.success(function (data) {
                    self.alertTitle = data.msg;
                    if (data.ret == '1') {
                        self.getDictPagePlus();
                        self.variantType = 'success';
                    } else {
                        self.variantType = 'danger';
                    }
                    self.showDismissibleAlert = 2;
                    // self.dismissCountDown = 1;
                    self.modalChangeDict.form = {
                        pId: '',
                        id: '',
                        sort: '',
                        name: ''
                    }

                });
                self.modalAdd.show = false;

            },
            modalAddCancel: function () {
                this.modalChangeDict.form = {
                    name: '',
                    id: '',
                    sort: '',
                    pId: ''
                };
                this.modalAdd.show = false;
                this.dictNameShow = false;
                this.dictFormName = '';
                $('.error').remove();
            },
            formSearch: function () {
                var self = this;
                var metaKey = this.metaKey;
                var typeid = this.dictItem.typeid || '';
                var xhr = $.post('/a/sys/dict/getDictPagePlus', {
                    typeid: typeid,
                    name: $.trim(metaKey),
                    pageNo: this.currentPage,
                    pageSize: this.pageSize
                });

                xhr.success(function (data) {
                    self.total = data.count;
                    self.projectTypes = data.list;
                    self.currentPage = data.pageNo;
                    self.pageSize = data.pageSize;
                })
            },
            formCreate: function () {
                this.modalAddEdit.title = '创建类别';
                this.modalAddEdit.show = true;
                this.ptTypeVal = 0;
                this.ptTypeName = '';
            },

            pChange: function (pager) {
                this.currentPage = pager;
                this.getDictPagePlus();
            },
            pSelect: function (val, currentPage) {
                this.pageSize = val;
                this.currentPage = currentPage;
                this.getDictPagePlus();

            },
            selectChange: function (event) {
                var val = event.target.val;
                if (val) {
                    $(event.target).next().remove()
                }
            },
            getDictJson: function () {
                var _self = this;
                var xhr = $.get('/a/sys/dict/getDictTypeListPlus');
                xhr.success(function (data) {
                    _self.dictjson = data;
                    _self.filterDicts = _self.dictjson.slice(0);
                    _self.filterDictsAdd = _self.dictjson.slice(0);
                })
            },
            getDictPagePlus: function () {
                var _self = this;
                var postData = {
                    pageNo: this.currentPage,
                    pageSize: this.pageSize,
                    typeid: this.dictItem.typeid || '',
                    name: this.dictItem.name || ''
                };

                var xhr = $.post('/a/sys/dict/getDictPagePlus', postData);
                xhr.success(function (data) {
                    _self.total = data.count;
                    _self.projectTypes = data.list;
                    _self.currentPage = data.pageNo;
                    _self.pageSize = data.pageSize;
                    if (!_self.dataLoad) {
                        _self.dataLoad = true;
                    }
                });
                // this.$nextTick(function () {
                //     setTimeout(function () {
                //         _self.autoLeftMenuHeight();
                //     }, 100)
                // })
            },
            changeDictJson: function (item, index) {
                // var $curEle = $(this.$refs[item.id + index]);
                // var top = $curEle.position().top;
                // var $curEleParent = $curEle.parents('ul');
                this.currentDictIndex = index;
                this.dictItem.typeid = item.id;
                this.dictItem.name = '';
                this.modalChangeDict.form.id = item.id;
                // this.dictName = item.label;
                this.dictNamePlaceholder = item.label;
                // $curEleParent.scrollTop(top);
                this.getDictPagePlus();
            },
            clearDictName: function () {
                this.dictName = '';
                this.dictItem.name = '';
                this.dictItem.typeid = '';
                this.currentDictIndex = '';

                if (!this.metaKey) {
                    this.getDictPagePlus()
                }
                this.metaKey = '';
                // this.getDictJson();
            },

            changeDictName: function () {
                if (!this.dictName) {
                    this.clearDictName()
                }
            },
            edtDictType: function (dicType) {
                this.modalChangeDictTypeTitle = '修改字典类型';
                this.edtDictTypeAble = false;
                this.modalChangeDictType.show = true;
                this.modalChangeDictType.form.id = dicType.id;
                this.modalChangeDictType.form.name = dicType.label;
                this.modalChangeDictType.form.sort = dicType.sort;
                this.changeType = 'edtDictType'
            },
            addDictType: function (item) {
                this.modalChangeDict.form.pId = item.id;
                this.modalAdd.show = true;
            },
            addDictTypeTop: function () {
                this.modalChangeDictTypeTitle = '添加字典类型';
                this.edtDictTypeAble = false;
                this.modalChangeDictType.show = true;
                this.changeType = 'addDictType';
            },
            delDictType: function (item) {
                this.delText = '确认要删除这条字典类型吗？';
                this.modalDelete.title = '删除字典类型';
                this.modalDelete.show = true;
                this.delItem = item;


            },
            modalDeleteOk: function () {
                var self = this;
                var xhr = $.post('/a/sys/dict/delDictType', {id: this.delItem.id});
                xhr.success(function (data) {
                    self.alertTitle = data.msg;
                    self.variantType = 'danger';
                    self.showDismissibleAlert = 2;
                    // self.dismissCountDown = 1;
                    // self.currentDictIndex = -1;
                    // self.dictItem.typeid = '';
                    // self.dictItem.name = '';
                    // self.currentPage = 1;
                    self.changeType = '';
                    self.getDictJson();
                    self.getDictPagePlus();
                });

                this.modalDelete.show = false;
            },
            modalDeleteCancel: function () {
                this.changeType = '';
                this.modalDelete.show = false;
            },
            dictTypeInputChange: function (event) {
                if (this.modalChangeDictType.form.name) {
                    $(event.target).next().remove()
                }
            },
            modalChangeDictOk: function () {
                var $modalChangeDictTypeId = $('#modalChangeDictTypeId');
                if (!this.modalChangeDictType.form.name) {
                    if ($modalChangeDictTypeId.next().size() < 1) {
                        $modalChangeDictTypeId.after('<label class="error" style="color:red">请填写字典类型名称</label>');
                    }
                    return false;
                }
                this.postDictName();
                this.modalChangeDictType.show = false;
            },
            addDictName: function () {
                this.changeType = 'addDictType';
                this.postDictName();
            },
            postDictName: function () {
                var url = '/a/sys/dict/' + this.changeType;
                var postData;
                var self = this;
                if (this.changeType == 'edtDictType') {
                    postData = {
                        id: this.modalChangeDictType.form.id,
                        name: this.modalChangeDictType.form.name
                    };
                } else if (this.changeType == 'addDictType') {
                    postData = {
                        name: this.dictName
                    };
                }
                var xhr = $.post(url, postData);
                this.dictName = '';
                xhr.success(function (data) {
                    if (data.ret == '1') {
                        self.getDictJson();
                        self.variantType = 'success';
                    } else {
                        self.variantType = 'danger';
                    }
                    self.alertTitle = data.msg;
                    self.showDismissibleAlert = 2;
                    // self.dismissCountDown = 1;
                    self.modalChangeDictType.form = {
                        id: '',
                        name: ''
                    };
                });
            },

            modalChangeCancel: function () {
                this.modalChangeDictType.form = {
                    name: '',
                    id: '',
                    sort: ''
                };
                this.modalChangeDictType.show = false;
                $('.error').remove();
            },
            countDownChanged: function (dismissCountDown) {
                this.showDismissibleAlert = dismissCountDown;
            },

            autoLeftMenuHeight: function () {
                var $centerContent = $('.center-content');
                // var centerContentHeight = $centerContent.innerHeight();
                var $bdNav = $('.bd-nav');
                var winHeight = $(window).height() - 200;
                $bdNav.css('height', winHeight)
                // if (this.pageSize > 10 && winHeight < centerContentHeight) {
                //     $bdNav.css('max-height', centerContentHeight - 100)
                // } else {
                //     $bdNav.css('max-height', winHeight)
                // }
            }
        },
        transitions: {
            collapse: {
                enter: function (el) {
                    var nav = el.querySelector('.bd-nav');
                    var height = nav.offsetHeight;
                    el.style.height = height + 'px';
                },
                leave: function (el) {
                    var nav = el.querySelector('.bd-nav');
                    el.style.height = '';
                }
            }
        },
        beforeMount: function () {
            this.getDictJson();
            this.getDictPagePlus();
        },
        mounted: function () {
            this.autoLeftMenuHeight();
        }
    });
    return baseData;
});

