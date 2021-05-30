package swaggy;
import javax.swing.*;
import java.awt.*;
import java.util.Random;



public class MasterPanel {

    private int randomNum;
    private int frameTracker;
    private final int[][] coordinates;

    private int current=0;


    private final JButton rollBtn;
    private final JButton snakeLadderCaseBtn;
    private final JButton restartBtn;



    private boolean overflowBool =false;

    private final JPanel diePanel;
    private final JPanel boardPanel;


    private final JLabel backImgLabel;
    private final JLabel winLabel;

    private int once=0;

    //checks by how much player has to go back in case of overflow
    private int backward=0;

    //
    private final JLabel die1Label;
    private final JLabel die2Label;
    private final JLabel die3Label;
    private final JLabel die4Label;
    private final JLabel die5Label;
    private final JLabel die6Label;
    private final JLabel playerLabel;

    public MasterPanel(){

        //jbuttons initialization
        rollBtn =new JButton("Roll");
        restartBtn =new JButton("Restart");
        rollBtn.setBounds(50,60,100,50);
        restartBtn.setBounds(50,60,100,50);
        snakeLadderCaseBtn =new JButton();

        //player emoji coordinates on map
        coordinates = new int[][]{{-20, 375}, {70,375}, {160,375}, {250,375}, {340,375}, {430,375}, {520,375}, {-20,285}, {70,285}, {160,285}, {250,285}, {340,285}, {430,285}, {520,285}, {-20,195}, {70,195}, {160,195}, {340,195}, {430,195}, {520,195}, {-20,105}, {70,105}, {160,105}, {250,105}, {340,105}, {430,105}, {520,105}, {-20,15}, {70,15}, {160,15}, {250,15}, {340,15}, {430,15}, {520,15}};

        //die pictures
        die1Label =new JLabel(new ImageIcon("die1.PNG"));
        die2Label =new JLabel(new ImageIcon("die2.PNG"));
        die3Label =new JLabel(new ImageIcon("die3.PNG"));
        die4Label =new JLabel(new ImageIcon("die4.PNG"));
        die5Label =new JLabel(new ImageIcon("die5.PNG"));
        die6Label =new JLabel(new ImageIcon("die6.PNG"));
        playerLabel =new JLabel(new ImageIcon("player.PNG"));

        //die coordinates
        die1Label.setBounds(250,44,150,150);
        die2Label.setBounds(250,44,150,150);
        die3Label.setBounds(250,44,150,140);
        die4Label.setBounds(250,44,150,150);
        die5Label.setBounds(250,44,150,150);
        die6Label.setBounds(250,44,150,150);

        //board and win case background
        ImageIcon backImage = new ImageIcon("board.png");
        ImageIcon winImage = new ImageIcon("win.jpg");
        backImgLabel =new JLabel(backImage);
        winLabel =new JLabel(winImage);
        winLabel.setBounds(0,0,659,540);
        backImgLabel.setBounds(0,0,659,540);

        //JFrame initialization
        JFrame frame = new JFrame("Snake And Ladder");
        frame.setSize(659,705);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setIconImage(backImage.getImage());

        //board panel initialization
        boardPanel=new JPanel();
        boardPanel.setBounds(-7,-37,659,505);
        boardPanel.add(backImgLabel);

        //die panel initialization
        diePanel=new JPanel();
        diePanel.setBounds(-7,465,659,200);
        diePanel.setBackground(Color.CYAN);
        diePanel.add(rollBtn);

        //add panel to JFrame
        frame.add(boardPanel);
        frame.add(diePanel);

        rollBtn.addActionListener(e -> {

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        //add current die on the panel and player emoji at the right place
                        diePanel.removeAll();
                        boardPanel.add(playerLabel);
                        boardPanel.add(backImgLabel);
                        diePanel.add(rollBtn);
                        diePanel.repaint();
                        boardPanel.repaint();

                        //generate random die
                        randomNum=randomNumberGenerator();
                        switch (randomNum) {
                            case 1 -> diePanel.add(die1Label);
                            case 2 -> diePanel.add(die2Label);
                            case 3 -> diePanel.add(die3Label);
                            case 4 -> diePanel.add(die4Label);
                            case 5 -> diePanel.add(die5Label);
                            case 6 -> diePanel.add(die6Label);
                        }

                        //get player emoji position
                        try {
                            Tracker();
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }


                        //checks if player is not out of bounds, if not it moves according to die
                        if(overflowBool){
                            while (current!= coordinates.length)
                            {
                                rollBtn.setEnabled(false);
                                playerLabel.setBounds(coordinates[current][0], coordinates[current][1],150,150);
                                current++;
                                Thread.sleep(500);
                            }
                            current= coordinates.length-1;

                            while (backward!=0)
                            {
                                rollBtn.setEnabled(false);
                                current--;
                                playerLabel.setBounds(coordinates[current][0], coordinates[current][1],150,150);
                                Thread.sleep(500);
                                backward--;
                            }
                            frameTracker=current+1;
                        }

                        else {
                            while (frameTracker> current) {
                                rollBtn.setEnabled(false);
                                playerLabel.setBounds(coordinates[current][0], coordinates[current][1], 150, 150);//update player position
                                current++;
                                Thread.sleep(500);
                            }
                             if(frameTracker==34){//win case
                                rollBtn.setEnabled(true);
                                while (once<1) {
                                    rollBtn.doClick();
                                    once++;
                                }
                            }
                        }

                        overflowBool =false;

                        //checks if player is on a ladder or a snake
                        if(frameTracker==2||frameTracker==4||frameTracker==8||frameTracker==9||frameTracker==10||frameTracker==13
                                ||frameTracker==15||frameTracker==18||frameTracker==25||frameTracker==28||frameTracker==30||frameTracker==32)
                        {
                            snakeLadderCaseBtn.doClick();
                        }
                        else{
                            rollBtn.setEnabled(true);
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                }

            });
            t.start();
        });

        //listener that deal with snake and ladder cases
        snakeLadderCaseBtn.addActionListener(e -> {

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        try {
                            Tracker();
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        //update player position
                        playerLabel.setBounds(coordinates[frameTracker-1][0], coordinates[frameTracker-1][1],150,150);

                        diePanel.repaint();
                        boardPanel.repaint();

                        //checks if player is on a ladder or a snake
                        Thread.sleep(500);
                        if(frameTracker==2||frameTracker==4||frameTracker==8||frameTracker==9||frameTracker==10||frameTracker==13
                                ||frameTracker==15||frameTracker==18||frameTracker==25||frameTracker==28||frameTracker==30||frameTracker==32)
                        {
                            snakeLadderCaseBtn.doClick();
                        }


                        rollBtn.setEnabled(true);
                        current=frameTracker;

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                }

            });
            t.start();
        });

        //reinitialize everything like it was at the beginning
        restartBtn.addActionListener(e -> {

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                   current=0;
                   once=0;
                   frameTracker=0;
                   diePanel.removeAll();
                   boardPanel.removeAll();
                   boardPanel.setBackground(Color.white);
                   boardPanel.add(backImgLabel);
                   diePanel.add(rollBtn);
                   diePanel.repaint();
                   boardPanel.repaint();
                }

            });
            t.start();
        });



    }

    //method that generate a number 1 to 6
    private int randomNumberGenerator(){
        int num;
        Random random = new Random();
        do {
            num = random.nextInt(7);
        } while (num == 0);
        return num;
    }

    //method that that keeps track of player positioning depending where player lands
    private void Tracker( ) throws InterruptedException {
        switch (frameTracker) {
            case 2:
                frameTracker=31;
                break;
            case 4:
                frameTracker=12;
                break;
            case 8:
                frameTracker=2;
                break;
            case 9:
                frameTracker=23;
                break;
            case 10:
                frameTracker=4;
                break;
            case 13:
                frameTracker=27;
                break;
            case 15:
                frameTracker=29;
                break;
            case 18:
                frameTracker=25;
                break;
            case 25:
            case 30:
                frameTracker=6;
                break;
            case 28:
                frameTracker=16;
                break;
            case 32:
                frameTracker=19;
                break;
            case 34:
                //show that player one board
                diePanel.removeAll();
                boardPanel.removeAll();
                boardPanel.setBackground(Color.black);
                boardPanel.add(winLabel);
                diePanel.add(restartBtn);
                break;

            default:
                //checking overboard case
                if(frameTracker+randomNum>34){
                    //use to know by how much to go forward and back in case player goes overboard
                    int forward = 34 - frameTracker;
                    backward=randomNum- forward;
                    overflowBool =true;
                }else{
                    frameTracker+=randomNum;
                }

        }

    }


}
