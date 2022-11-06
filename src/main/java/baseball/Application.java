package baseball;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;
import com.sun.security.jgss.GSSUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현

        // 게임 진행 상태
        boolean usersAnswer = true;

        while (usersAnswer){

            // 컴퓨터가 3자리 숫자의 리스트 생성
            List<Integer> computerNumber = GenerateRandomNumber();
            // 유저의 문자 입력 받기
            String usersNumber = usersNumber();

            // 입력 받은 문자열 검증 <- 부정 표현 추후 리팩토링
            if(!validationUsersNumber(usersNumber)){
                // 잘못된 입력 값 시 IllegalStateException 발생
                throw new IllegalStateException();
            }

            // 볼,스트라이크 체크
            List<Integer> result = checkStrikeOrBall(computerNumber, usersNumber);

            // 결과 출력
            announcedResult(result);

            // 정답
            if (isRight(result.get(1))) {
                // 정답 시 재시작/종료 탐색 기능
                usersAnswer = reGameOrEnd(usersNumber());
            }


        }

    }

    private static boolean isRight(Integer strike) {
        if (strike == 3) {
            return true;
        }
        return false;
    }

    private static void announcedResult(List<Integer> result) {

        Integer ball = result.get(0);
        Integer strike = result.get(1);

        if(strike == 3){
            System.out.println(strike+"스트라이크");
            System.out.println("3개의 숫자를 모두 맞히셨습니다! 게임 종료");
            System.out.println("게임을 새로 시작하려면 1, 종료하려면 2를 입력하세요.");
        }
        else if (ball > 0 && strike > 0) {
            System.out.println(ball+"볼 "+strike+"스트라이크");
        } else if (ball > 0 && strike == 0) {
            System.out.println(ball+"볼");
        } else if (ball == 0 && strike > 0) {
            System.out.println(strike+"스트라이크");
        }

    }

    private static List<Integer> checkStrikeOrBall(List<Integer> computerNumber, String usersNumber) {

        int ball = 0;
        int strike = 0;

        for (int i = 0; i < usersNumber.length(); i++) {

            int number = Character.getNumericValue(usersNumber.charAt(i));

            boolean isBall = isBall(computerNumber, number);
            boolean isStrike = false;
            
            if(isBall){
                isStrike = isStrike(computerNumber, number, i);
            }

            if(isStrike){
                strike++;
                continue;
            }
            
            if(isBall){
                ball++;
            }

        }

        return List.of(ball, strike);
    }

    private static boolean isBall(List<Integer> computerNumber, int number) {
        if (computerNumber.contains(number)){
            return true;
        }
        return false;
    }

    private static boolean isStrike(List<Integer> computerNumber, int number , int index) {

        if(!computerNumber.contains(number)){
            return false;
        }

        if(computerNumber.indexOf(number) == index){
            return true;
        }

        return false;
    }


    private static boolean validationUsersNumber(String usersNumber) {

        // 입력한 문자열의 길이가 3자리가 아니라면 false
        if (usersNumber.length() != 3) {
            System.out.println("세자리 아님" + usersNumber);
            return false;
        }

        // 정규 표현식 숫자가 아닐 때 ! <- 부정 표현 추후 리팩토링
        if (!Pattern.matches("^[0-9]+$", usersNumber)) {
            System.out.println("숫자가 아님! " + usersNumber);
            return false;
        }

        return true;
    }

    private static boolean reGameOrEnd(String usersAnswer) {
        if(usersAnswer.equals("1")){
            return true;
        }else{
            return false;
        }
    }

    private static String usersNumber() {
        return Console.readLine();
    }

    private static List<Integer> GenerateRandomNumber() {

        List<Integer> list = new ArrayList<>();

        while (list.size() < 3) {
            int number = Randoms.pickNumberInRange(1, 9);

            // 숫자 중복 검사
            if(isNotDuplicatedNumber(list , number)){
                list.add(number);
            }
        }

        return list;
    }

    private static boolean isNotDuplicatedNumber(List<Integer> list , int number) {
        if(list.contains(number)){
            return false;
        }else {
            return true;
        }
    }
}
