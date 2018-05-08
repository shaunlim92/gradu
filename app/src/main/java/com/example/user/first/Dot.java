package com.example.user.first;

/**
 * Created by 종수 on 2017-12-05.
 */

public class Dot {
    /*case dafda
    0 초 중 종2
    1 초 중 종1
    2 초 중
    3 초 중2 종2
    4 초 중2 종1
    5 초 중2
    6    중 종2
    7    중  종1
    8    중
    9   중2 종2
    10   중2 종1
    11   중2
    12 초2 중 종2
    13 초2 중 종1
    14 초2 중
    15 초2 중2 종2
    16 초2 중2 종1
    17 초2 중2
     */
    int whatcase=0;
    byte cb_cho1, cb_cho2, cb_jung1, cb_jung2, cb_jong1, cb_jong2;
    String ch_cho, ch_jung, ch_jong;       //바이트를 리턴할 때 문자도 같이 리턴할 생각.
    //자바 생성자 초성중성 종성을 받아서 case별로 구분
    public Dot(int cb_cho, int cb_jung, int cb_jong){ // 초성 중성 종성을 받아서 점자로 출력하게해줌 cb=converbraill의 약자
        //초성이 'ㅇ'일 경우
        if(cb_cho==11) {
            whatcase = whatcase + 6;
        }
        //초성이 된소리 일 경우
        else if(cb_cho==1 || cb_cho==4 || cb_cho==8 || cb_cho==10 || cb_cho==13) {
            findcho2(cb_cho);
            whatcase=whatcase+12;
        }

        //초성이 1개인 경우
        else
            cb_cho1=findcho1(cb_cho);

        //중성이 ㅒ ㅙ ㅞ 일 경우
        if(cb_jung==3 || cb_jung==10 || cb_jung==15 || cb_jung==16) {
            whatcase = whatcase + 3;
            findjung1(cb_jung);
        }
        //중성 나머지
        else
            findjung1(cb_jung);

        //종성이 없는경우
        if(cb_jong == 0)
            whatcase = whatcase + 2;
        //종성이 1개인 경우
        else if(cb_jong == 1 || cb_jong == 4 || cb_jong == 7 || cb_jong == 8 || cb_jong == 16 || cb_jong == 17 || cb_jong == 19 || cb_jong == 21 ||
                cb_jong == 22 || cb_jong == 23 || cb_jong == 24 || cb_jong == 25 || cb_jong == 26 || cb_jong == 27)
        {
            whatcase++;
            cb_jong1=findjong1(cb_jong);
        }

        //종성이 2개인 경우
        else
            findjong2(cb_jong);
    }

    //출력시 byte로 나옴
    protected byte findcho1 (int cb_cho)
    {
        switch (cb_cho) {
            // ㄱ
            case 0:
                ch_cho = "ㄱ" ;
                return 0b001000;
            //ㄴ
            case 2:
                ch_cho = "ㄴ" ;
                return 0b001001;
            //ㄷ
            case 3:
                ch_cho = "ㄷ" ;
                return 0b001010;
            // ㄹ
            case 5:
                ch_cho = "ㄹ" ;
                return 0b010000;
            //ㅁ
            case 6:
                ch_cho = "ㅁ" ;
                return 0b010001;
            //ㅂ
            case 7:
                ch_cho = "ㅂ" ;
                return 0b011000;
            // ㅅ
            case 9:
                ch_cho = "ㅅ" ;
                return 0b100000;
            // ㅈ
            case 12:
                ch_cho = "ㅈ" ;
                return 0b101000;
            // ㅊ
            case 14:
                ch_cho = "ㅊ" ;
                return 0b110000;
            // ㅋ
            case 15:
                ch_cho = "ㅋ" ;
                return 0b001011;
            // ㅌ
            case 16:
                ch_cho = "ㅌ" ;
                return 0b010011;
            // ㅍ
            case 17:
                ch_cho = "ㅍ" ;
                return 0b011001;
            // ㅎ
            default:
                ch_cho = "ㅎ" ;
                return 0b011010;
        }

    }

    //초성 2개
    protected void findcho2 (int cb_cho)
    {
        //된소리
        cb_cho1=0b100000;
        //찾아보자이제 쌍자음에서1빼면 그자음
        cb_cho2=findcho1(cb_cho-1);
        switch(cb_cho-1){
            // ㄱ
            case 0:
                ch_cho = "ㄲ" ;
                break;
            //ㄷ
            case 3:
                ch_cho = "ㄸ" ;
                break;
            //ㅂ
            case 7:
                ch_cho = "ㅃ" ;
                break;
            // ㅅ
            case 9:
                ch_cho = "ㅆ" ;
                break;
            // ㅈ
            case 12:
                ch_cho = "ㅉ" ;
                break;

            default:
                ch_cho=" ";

        }
    }

    protected void findjung1(int cb_jung){
        switch(cb_jung){
            //ㅏ
            case 0: cb_jung1=0b100011;
                ch_jung = "ㅏ" ;
                break;
            //ㅐ
            case 1: cb_jung1=0b010111;
                ch_jung = "ㅐ" ;
                break;
            //ㅑ
            case 2: cb_jung1=0b011100;
                ch_jung = "ㅑ" ;
                break;
            //ㅒ
            case 3: cb_jung1=0b011100; cb_jung2=0b010111;
                ch_jung = "ㅒ" ;
                break;
            //ㅓ
            case 4: cb_jung1=0b001110;
                ch_jung = "ㅓ" ;
                break;
            //ㅔ
            case 5: cb_jung1=0b011101;
                ch_jung = "ㅔ" ;
                break;
            //ㅕ
            case 6: cb_jung1=0b110001;
                ch_jung = "ㅕ" ;
                break;
            //ㅖ
            case 7: cb_jung1=0b001100;
                ch_jung = "ㅖ" ;
                break;
            //ㅗ
            case 8: cb_jung1=0b100101;
                ch_jung = "ㅗ" ;
                break;
            //ㅠ
            case 17: cb_jung1=0b101001;
                ch_jung = "ㅠ" ;
                break;
            //ㅘ
            case 9: cb_jung1=0b100111;
                ch_jung = "ㅘ" ;
                break;
            //ㅛ
            case 12: cb_jung1=0b101100;
                ch_jung = "ㅛ" ;
                break;
            //ㅙ
            case 10: cb_jung1=0b100111; cb_jung2=0b010111;
                ch_jung = "ㅙ" ;
                break;
            //ㅚ
            case 11: cb_jung1=0b111101;
                ch_jung = "ㅚ" ;
                break;
            //ㅜ
            case 13: cb_jung1=0b001101;
                ch_jung = "ㅜ" ;
                break;
            //ㅝ
            case 14: cb_jung1=0b001111;
                ch_jung = "ㅟ" ;
                break;
            //ㅞ
            case 15: cb_jung1=0b001111; cb_jung2=0b010111;
                ch_jung = "ㅞ" ;
                break;
            //ㅟ
            case 16: cb_jung1=0b001101; cb_jung2=0b010111;
                ch_jung = "ㅟ" ;
                break;
            //ㅡ
            case 18: cb_jung1=0b101010;
                ch_jung = "ㅡ" ;
                break;
            //ㅢ
            case 19: cb_jung1=0b111010;
                ch_jung = "ㅢ" ;
                break;
            //ㅣ
            default: cb_jung1=0b010101;
                ch_jung = "ㅣ" ;
                break;
            }
       }

    //종성이 1개일때 종성값 리턴
    protected byte findjong1 (int cb_jong){
        // ㄱ
        if(cb_jong == 1) {
            ch_jong = "ㄱ" ;
            return 0b000001;
        }
         /*   || cb_jong == 4 || cb_jong == 7 || cb_jong == 8 || cb_jong == 16 || cb_jong == 17 || cb_jong == 19 || cb_jong == 21 ||
                cb_jong == 22 || cb_jong == 23 || cb_jong == 24 || cb_jong == 25 || cb_jong == 26 || cb_jong == 27*/
            // ㄴ
        else if( cb_jong == 4 ) {
            ch_jong = "ㄴ" ;
            return 0b010010;
        }
            // ㄷ
        else if( cb_jong == 7 ) {
            ch_jong = "ㄷ" ;
            return 0b010100;
        }
            //ㄹ
        else if( cb_jong == 8 ) {
            ch_jong = "ㄹ" ;
            return 0b000010;
        }
            //ㅁ
        else if( cb_jong == 16 ) {
            ch_jong = "ㅁ" ;
            return 0b100010;
        }
            //ㅂ
        else if( cb_jong == 17 ) {
            ch_jong = "ㅂ" ;
            return 0b000011;
        }
            //ㅅ
        else if( cb_jong == 19 ) {
            ch_jong = "ㅅ" ;
            return 0b000100;
        }
            //ㅇ
        else if( cb_jong == 21 ) {
            ch_jong = "ㅇ" ;
            return 0b110110;
        }
            //ㅈ
        else if( cb_jong == 22 ) {
            ch_jong = "ㅈ" ;
            return 0b000101;
        }
            //ㅊ
        else if( cb_jong == 23 ) {
            ch_jong = "ㅊ" ;
            return 0b000110;
        }
            //ㅋ
        else if( cb_jong == 24 ) {
            ch_jong = "ㅋ" ;
            return 0b010110;
        }
            //ㅌ
        else if( cb_jong == 25 ) {
            ch_jong = "ㅌ" ;
            return 0b100110;
        }
           //ㅍ
        else if(cb_jong == 26) {
            ch_jong = "ㅍ" ;
            return 0b110010;
        }
            //ㅎ
        else {
            ch_jong = "ㅎ" ;
            return 0b110100;
        }
    }

    //종성이 2개일때
    protected void findjong2(int cb_jong){
        switch (cb_jong) {
            //ㄲ
            case 2:
                cb_jong1=findjong1(1);
                cb_jong2=findjong1(1);
                ch_jong = "ㄲ" ;
                break;
            //ㄳ
            case 3:
                cb_jong1=findjong1(1);
                cb_jong2=findjong1(19);
                ch_jong = "ㄳ" ;
                break;
            //ㄵ
            case 5:
                cb_jong1=findjong1(4);
                cb_jong2=findjong1(22);
                ch_jong = "ㄵ" ;
                break;
            //ㄶ
            case 6:
                cb_jong1=findjong1(4);
                cb_jong2=findjong1(27);
                ch_jong = "ㄶ" ;
                break;
            //ㄺ
            case 9:
                cb_jong1=findjong1(8);
                cb_jong2=findjong1(1);
                ch_jong = "ㄺ" ;
                break;
            //ㄻ
            case 10:
                cb_jong1=findjong1(8);
                cb_jong2=findjong1(16);
                ch_jong = "ㄻ" ;
                break;
            //ㄼ
            case 11:
                cb_jong1=findjong1(8);
                cb_jong2=findjong1(17);
                ch_jong = "ㄼ" ;
                break;
            //ㄽ
            case 12:
                cb_jong1=findjong1(8);
                cb_jong2=findjong1(19);
                ch_jong = "ㄽ" ;
                break;
            //ㄾ
            case 13:
                cb_jong1=findjong1(8);
                cb_jong2=findjong1(25);
                ch_jong = "ㄾ" ;
                break;
            //ㄿ
            case 14:
                cb_jong1=findjong1(8);
                cb_jong2=findjong1(26);
                ch_jong = "ㄿ" ;
                break;
            //ㅀ
            case 15:
                cb_jong1=findjong1(8);
                cb_jong2=findjong1(27);
                ch_jong = "ㅀ" ;
                break;
            //ㅄ
            case 18:
                cb_jong1=findjong1(17);
                cb_jong2=findjong1(19);
                ch_jong = "ㅄ" ;
                break;
            //ㅆ
            case 20:
                //이부분 약어 있음 받침 ㅆ
                //cb_jong1=findjong1(20);
                //cb_jong2=findjong1(20);
                cb_jong1 = 0b001100;
                ch_jong = "ㅆ" ;
                break;

            }
        }
}
