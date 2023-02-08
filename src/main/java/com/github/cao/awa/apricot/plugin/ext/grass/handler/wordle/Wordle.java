package com.github.cao.awa.apricot.plugin.ext.grass.handler.wordle;

import com.github.cao.awa.apricot.identifier.*;
import com.github.cao.awa.apricot.resource.loader.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.cao.awa.apricot.util.file.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;
import java.util.*;

public class Wordle {
    private static final Map<Character, Integer> ORDER = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashMap(),
            map -> {
                char[] charArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
                for (int i = 0; i < charArray.length; i++) {
                    char c = charArray[i];
                    map.put(
                            c,
                            i
                    );
                }
            }
    );
    private static final Color YELLOW = new Color(
            201,
            180,
            87
    );
    private static final Color GRAY = Color.LIGHT_GRAY;
    private static final Color GREEN = new Color(
            107,
            199,
            99
    );
    private final int size = 64;
    private final int sp = 4;
    private final String word;
    private final List<Character> already = ApricotCollectionFactor.newArrayList();
    private final String identifier;
    private int rows = 1;
    private int lines = 1;
    private int curLines = 1;
    private Graphics2D mainGraphics;
    private BufferedImage main;
    private Graphics2D tipsGraphics;
    private BufferedImage tips;
    private boolean isDone = false;
    private String currentWord = "";
    private int trys = 0;

    public Wordle(String word) throws IOException, FontFormatException {
        this.word = word.toUpperCase();
        this.identifier = FileRandomIdentifier.create(16);
        if (word.length() == 0) {
            return;
        }
        newMain();
        newTips();
        for (char c : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
            drawTip(
                    c,
                    true,
                    false,
                    false
            );
        }
    }

    private void newMain() throws IOException, FontFormatException {
        BufferedImage result = new BufferedImage(
                this.sp + (this.sp * this.word.length() + this.size * this.word.length()) * this.rows + (this.rows * this.sp),
                this.sp + (this.size * this.lines + this.sp * this.lines),
                BufferedImage.TYPE_INT_ARGB
        );
        Font mono = Font.createFont(
                Font.TRUETYPE_FONT,
                ResourcesLoader.get("fonts/mono/JetBrainsMono-Bold.ttf")
        );

        Font font = mono.deriveFont(
                Font.PLAIN,
                this.size
        );

        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        environment.registerFont(font);

        Graphics2D graphics = environment.createGraphics(result);

        graphics.setFont(font);

        graphics.fillRect(
                0,
                0,
                result.getWidth(),
                result.getHeight()
        );
        if (this.main != null) {
            graphics.drawImage(
                    this.main,
                    0,
                    0,
                    null
            );
        }

        this.mainGraphics = graphics;
        this.main = result;
    }

    private void newTips() throws IOException, FontFormatException {
        int tipSize = this.size / 2;
        int charCount = this.main.getWidth() / (tipSize + 2);
        int lines = (int) Math.ceil(26D / charCount);
        BufferedImage result = new BufferedImage(
                this.sp + (charCount * tipSize) + (charCount * this.sp),
                lines * tipSize + this.sp * lines,
                BufferedImage.TYPE_INT_ARGB
        );
        Font mono = Font.createFont(
                Font.TRUETYPE_FONT,
                ResourcesLoader.get("fonts/mono/JetBrainsMono-Bold.ttf")
        );

        Font font = mono.deriveFont(
                Font.PLAIN,
                tipSize
        );

        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        environment.registerFont(font);

        Graphics2D graphics = environment.createGraphics(result);

        graphics.setFont(font);

        graphics.fillRect(
                0,
                0,
                result.getWidth(),
                result.getHeight()
        );

        this.tipsGraphics = graphics;
        this.tips = result;
    }

    private void drawTip(char c, boolean white, boolean match, boolean has) {
        int order = ORDER.get(c);
        if (this.already.contains(c)) {
            return;
        }

        Color backgroundColor = white ? Color.WHITE : match ? GREEN : has ? YELLOW : GRAY;
        int width = this.main.getWidth() - this.sp;
        int tipSize = this.size / 2;

        int ine = (width / (tipSize + this.sp));
        int x = this.sp + (order * this.sp) + (order * tipSize);
        int y = 0;
        int l = 0;
        while (true) {
            if (x >= width) {
                order = order - ine - 1;
                x = this.sp + (order * this.sp) + (order * tipSize);
                y = this.sp + (++ l * tipSize);
            } else {
                break;
            }
        }

        drawChar(
                this.tipsGraphics,
                c,
                backgroundColor,
                Color.BLACK,
                x,
                y,
                tipSize
        );

        if (match) {
            this.already.add(c);
        }
    }

    private static void drawChar(Graphics2D graphics, char text, Color backgroundColor, Color textColor, int x, int y, int size) {
        int hOff = size / 10 + size / 100;

        graphics.setPaint(backgroundColor);
        graphics.fillRect(
                x,
                y,
                size,
                size
        );

        graphics.setPaint(textColor);
        graphics.drawString(
                String.valueOf(text),
                x + hOff * 2,
                y + size - hOff
        );
    }

    public String getWord() {
        return this.word;
    }

    public boolean draw(String str) throws IOException, FontFormatException {
        if (this.isDone) {
            return false;
        }
        if (str.length() != this.word.length()) {
            return false;
        }
        Map<Character, Integer> charCounts = ApricotCollectionFactor.newHashMap();
        for (char c : this.word.toCharArray()) {
            charCounts.put(
                    c,
                    charCounts.getOrDefault(
                            c,
                            0
                    ) + 1
            );
        }

        this.trys++;
        str = str.toUpperCase();
        this.currentWord = str;

        this.mainGraphics.setColor(Color.WHITE);

        int baseX = (this.rows - 1) * (this.word.length() * this.size + this.word.length() * this.sp) + (this.rows - 1) * this.sp;

        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            boolean match = c == this.word.charAt(i);
            boolean has = this.word.contains(String.valueOf(c));
            boolean hasMore = charCounts.getOrDefault(
                    c,
                    0
            ) > 0;
            charCounts.put(
                    c,
                    charCounts.getOrDefault(
                            c,
                            0
                    ) - 1
            );
            drawTip(
                    c,
                    false,
                    match,
                    has
            );
            drawChar(
                    this.mainGraphics,
                    c,
                    hasMore ? match ? GREEN : has ? YELLOW : GRAY : GRAY,
                    Color.WHITE,
                    this.sp + (this.sp * i + this.size * i) + baseX,
                    this.sp + (this.curLines - 1) * (this.sp + this.size),
                    this.size
            );
        }

        this.isDone = this.word.equals(str);

        compound();

        if (this.curLines > 10) {
            this.rows++;
            this.curLines = 0;
        } else {
            if (this.curLines >= this.lines) {
                this.lines++;
            }
        }
        if (! this.isDone) {
            this.curLines++;
            newMain();
        }

        return true;
    }

    private void compound() throws IOException {
        File file = new File("wordles/" + this.identifier + "/" + this.trys + "-" + this.currentWord + ".png");
        FileUtil.mkdirsParent(file);

        BufferedImage image = this.main;
        if (! this.isDone) {
            image = new BufferedImage(
                    Math.max(
                            this.main.getWidth(),
                            this.tips.getWidth()
                    ),
                    this.main.getHeight() + this.tips.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D graphics = image.createGraphics();
            graphics.fillRect(
                    0,
                    0,
                    image.getWidth(),
                    image.getHeight()
            );
            graphics.drawImage(
                    this.main,
                    0,
                    0,
                    null
            );
            graphics.drawImage(
                    this.tips,
                    0,
                    this.main.getHeight(),
                    null
            );
        }

        ImageIO.write(
                image,
                "png",
                file
        );
    }

    public boolean isDone() {
        return this.isDone;
    }

    public File current() {
        return new File("wordles/" + this.identifier + "/" + this.trys + "-" + this.currentWord + ".png");
    }

    public String getIdentifier() {
        return this.identifier;
    }
}
