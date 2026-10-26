package main;

public record CellConstraint(LetterType type, String allowed, String banned){

    public static final CellConstraint NONE = new CellConstraint(LetterType.ANY, "", "");

    public boolean accepts(char c){
        boolean vowel = "AEIOU".indexOf(c) >= 0;
        if(type == LetterType.VOWEL && !vowel) return false;
        if(type == LetterType.CONSONANT && vowel) return false;
        if(!allowed.isEmpty() && allowed.indexOf(c) < 0) return false;

        return banned.indexOf(c) < 0;
    }

    public boolean isNone(){
        return type == LetterType.ANY && allowed.isEmpty() && banned.isEmpty();
    }
}
