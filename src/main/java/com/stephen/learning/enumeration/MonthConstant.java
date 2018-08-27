package com.stephen.learning.enumeration;

/**
 * @Auther: jack
 * @Date: 2018/8/27 21:19
 * @Description: 月份常量
 */
public interface MonthConstant {
    int getMonth();

    enum Indonesian implements MonthConstant{
        Januari(1),
        Februari(2),
        Maret(3),
        April(4),
        Mei(5),
        Juni(6),
        Juli(7),
        Agustus(8),
        September(9),
        Oktober(10),
        November(11),
        Desember(12);

        private int month;

        Indonesian(int month){
            this.month=month;
        }

        @Override
        public int getMonth(){
            return this.month;
        }

    }

    enum English implements MonthConstant{
        January(1),
        February(2),
        March(3),
        April(4),
        May(5),
        June(6),
        July(7),
        August(8),
        September(9),
        October(10),
        November(11),
        December(12);

        private int month;

        English(int month){
            this.month=month;
        }

        @Override
        public int getMonth(){
            return this.month;
        }
    }
}
