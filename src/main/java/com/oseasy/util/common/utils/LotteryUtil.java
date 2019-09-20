package com.oseasy.util.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

/***
 *  这N个专家存在一个数组中，随机打乱（保证每个专家出现在每个位置的概率都相同），
 然后选取前k个就行。具体的算法，参考用stl algorithm里的random_shuffle 洗牌
 */
public class LotteryUtil {
    static Map<String, List<String>> map = Maps.newHashMap(); //统计每个专家组审核的项目数
    static List sortList = Lists.newArrayList();


    public static void sortSpecsList(List sortList) {
        Collections.sort(sortList, new Comparator<Res>() {
            @Override
            public int compare(Res o1, Res o2) {
                List<Integer> list1 = (o1 == null)?new ArrayList<>():o1.getList();
                List<Integer> list2 = (o2 == null)?new ArrayList<>():o2.getList();
                for (int i = 0; i < o1.getList().size(); i++) {
                    if (list1.get(i) > list2.get(i)) {
                        return 1;
                    } else if (list1.get(i) < list2.get(i)) {
                        return -2;
                    } else {
                        continue;
                    }
                }
                return 0;
            }
        });
    }

    /**
     * @param gradeSpeEachTotal  每个项目需要多少个专家打分
     * @param gradeCancelTotal   每组专家不能同时审核多少个项目【默认为-1 未设置限制】
     * @param takeSpecs 参与评审的专家集合
     * @param specId  专家id
     * @return result 【如果为空 表示项目未分配到合适的专家组】
     */
    public static ArrayList getArrayList(int gradeSpeEachTotal, int gradeCancelTotal, ArrayList takeSpecs, String specId) {
        ArrayList result= Lists.newArrayList();
        Boolean vali=true;
        int step=0;
        while(vali){
            if (step>=500){ //防止没有分配到专家导致死循环。
                vali=false;
                break;
            }
            step++;
            ArrayList newresult = getListByShuffle(takeSpecs,gradeSpeEachTotal);//随机分配专家
            String mkey=setKey(result);
            List list = map.get(mkey) != null ? map.get(mkey) : Lists.newArrayList();
            //gradeCancelTotal=-1 表示 未设置限制
            if (gradeCancelTotal==-1 ||map.get(mkey)==null   ||    map.get(mkey).size() < gradeCancelTotal) {//分配的专家如果同时审核gradeCancelTotal个项目，作废，重新洗牌抽取
                list.add(specId);//项目集合
                map.put(mkey, list);
                System.out.printf("项目%s 分配的【有效】专家是 ,%s \n",specId,result.toString());
                result=newresult;
                //汇总抽取的结果，方便后面验证结果是否正确
                LotteryUtil.Res res = new LotteryUtil.Res(specId, result);
                sortList.add(res);
                vali=false;
                break;
            }else{
                System.out.printf("项目%s 分配的【无效】专家是 ,%s \n",specId,result.toString());
            }
        }//end while
        return result;
    }

    public static  String setKey(ArrayList result) {
        StringBuffer skey = new StringBuffer();
        for (Object i:result ) {
            skey.append(i+"|");
        }
        return  skey.toString();
    }

    /**
     * 洗牌 获取前total个元素
     * @param list   待洗牌的集合
     * @param num  获取的元素总数
     * @return
     */
    public static ArrayList getListByShuffle(ArrayList list, int num) {
        Collections.shuffle(list);
        ArrayList result = Lists.newArrayList(list.subList(0, num ));
        Collections.sort(result);
        return result;
    }




    public static class Res {
        String id;  //项目ID
        List list;  //分配的专家
        public Res() {
        }
        public Res(String id, List list) {
            this.id = id;
            this.list = list;
        }
        @Override
        public String toString() {
            return "Res{" +
                    "id=" + id +
                    ", list=" + list +
                    "}   \n";
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List getList() {
            return list;
        }

        public void setList(List list) {
            this.list = list;
        }
    }

}
