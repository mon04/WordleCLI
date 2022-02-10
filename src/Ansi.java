public enum Ansi {
    GREEN ("\033[38;5;15;48;5;22m"),
    YELLOW ("\033[38;5;15;48;5;94m"),
    GRAY ("\033[37;48;5;234m"),
    RED ("\033[38;5;15;48;5;88m"),
    RESET ("\033[0m");

    private final String esc; //Escape code

    Ansi(String esc) {
        this.esc=esc;
    }

    public String toString() {
        return esc;
    }
}