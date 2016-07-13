/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selegram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author SKings
 */
public class searchPanel extends javax.swing.JPanel {

    /**
     * Creates new form searchPanel
     */
    Box box;
    MainFrame mainFrame;
    private String type;
    private ArrayList<JRadioButton> friends;
    private ArrayList<MessageCloud> hashtags;

    public searchPanel(MainFrame mainFrame) {
        initComponents();
        this.mainFrame = mainFrame;
        customInit();
    }

    public void customInit() {
        search_addfrirend_btn.setVisible(false);
        search_send_btn.setVisible(false);

        jScrollPane1.getVerticalScrollBar().setAutoscrolls(true);
        resultPanel.setLayout(new BorderLayout());
        box = Box.createVerticalBox();
        resultPanel.add(box, BorderLayout.NORTH);
        resultPanel.revalidate();
        resultPanel.repaint();
//        for (int i = 0; i < 20; i++) {
//            mainFrame.addToResultBox(resultPanel, box, new JButton("btn "+i));
//        }
//        mainFrame.addToResultBox(resultPanel, box, new JLabel("yes"));

    }

    public ArrayList<JRadioButton> searchPersons(String name) {
        box.removeAll();
        ArrayList<JRadioButton> persons = new ArrayList<>();
        ButtonGroup bG = new ButtonGroup();
        JSONObject obj = new JSONObject();
        obj.put("command", "search_name").put("options", new JSONObject().put("name", name));
        String result = mainFrame.clientHandler.getAnswer(obj.toString());
        JSONObject res = new JSONObject(result);
        JSONArray result_arr = res.getJSONObject("payload").getJSONArray("username");
        for (int i = 0; i < result_arr.length(); i++) {
            JRadioButton jrb = new JRadioButton(result_arr.getString(i));
            jrb.setFont(new Font("Calibri", 0, 18));
            jrb.setBackground(Color.white);
            persons.add(jrb);
        }
        for (int i = 0; i < persons.size(); i++) {
            bG.add(persons.get(i));
            mainFrame.addToResultBox(resultPanel, box, persons.get(i));
        }
        return persons;
    }

    public ArrayList<MessageCloud> searchHashtags(String text) {
        box.removeAll();
        ArrayList<MessageCloud> hashtags = new ArrayList<>();
        JSONObject obj = new JSONObject();
        obj.put("command", "search_hashtag").put("options", new JSONObject().put("username", mainFrame.user.getUsername()).put("hashtag", text));
        String result = mainFrame.clientHandler.getAnswer(obj.toString());
        JSONObject res = new JSONObject(result);
        JSONArray msgs = res.getJSONObject("payload").getJSONArray("messages");
        for (int i = 0; i < msgs.length(); i++) {
            hashtags.add(new MessageCloud(msgs.getJSONObject(i).getString("sender"), msgs.getJSONObject(i).getString("body")));
        }
        /*hashtags.add(new MessageCloud("Akbar", "hi"));
         hashtags.add(new MessageCloud("Akbar", "hi"));
         hashtags.add(new MessageCloud("Akbar", "hi"));
         hashtags.add(new MessageCloud("Akbar", "hi"));
         hashtags.add(new MessageCloud("Akbar", "hi"));
         hashtags.add(new MessageCloud("Akbar", "hi"));*/

        for (int i = 0; i < hashtags.size(); i++) {
            mainFrame.addToResultBox(resultPanel, box, hashtags.get(i));
        }
        return hashtags;
    }

    public JRadioButton getSelectedPerson() {
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).isSelected()) {
                return friends.get(i);
            }
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        search_addfrirend_btn = new javax.swing.JLabel();
        search_lbl2 = new javax.swing.JLabel();
        search_lbl1 = new javax.swing.JLabel();
        search_hashtag_btn = new javax.swing.JLabel();
        search_username_btn = new javax.swing.JLabel();
        search_username_text = new javax.swing.JTextField();
        search_hashtag_text = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultPanel = new javax.swing.JPanel();
        search_send_btn = new javax.swing.JLabel();
        home_btn = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setAutoscrolls(true);
        setPreferredSize(new java.awt.Dimension(700, 430));

        search_addfrirend_btn.setBackground(new java.awt.Color(255, 255, 255));
        search_addfrirend_btn.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        search_addfrirend_btn.setForeground(new java.awt.Color(0, 0, 0));
        search_addfrirend_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/selegram/add_friend.png"))); // NOI18N
        search_addfrirend_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                search_addfrirend_btnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                search_addfrirend_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                search_addfrirend_btnMouseExited(evt);
            }
        });

        search_lbl2.setBackground(new java.awt.Color(255, 255, 255));
        search_lbl2.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        search_lbl2.setForeground(new java.awt.Color(0, 0, 0));
        search_lbl2.setText("By username:");

        search_lbl1.setBackground(new java.awt.Color(255, 255, 255));
        search_lbl1.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        search_lbl1.setForeground(new java.awt.Color(0, 0, 0));
        search_lbl1.setText("By hashtag:");

        search_hashtag_btn.setBackground(new java.awt.Color(255, 255, 255));
        search_hashtag_btn.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        search_hashtag_btn.setForeground(new java.awt.Color(0, 0, 0));
        search_hashtag_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/selegram/search.png"))); // NOI18N
        search_hashtag_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                search_hashtag_btnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                search_hashtag_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                search_hashtag_btnMouseExited(evt);
            }
        });

        search_username_btn.setBackground(new java.awt.Color(255, 255, 255));
        search_username_btn.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        search_username_btn.setForeground(new java.awt.Color(0, 0, 0));
        search_username_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/selegram/search.png"))); // NOI18N
        search_username_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                search_username_btnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                search_username_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                search_username_btnMouseExited(evt);
            }
        });

        search_username_text.setBackground(new java.awt.Color(255, 255, 255));
        search_username_text.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        search_username_text.setForeground(new java.awt.Color(0, 0, 0));
        search_username_text.setText("Username");
        search_username_text.setToolTipText("");
        search_username_text.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(99, 167, 226)));
        search_username_text.setCaretColor(new java.awt.Color(99, 137, 226));

        search_hashtag_text.setBackground(new java.awt.Color(255, 255, 255));
        search_hashtag_text.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        search_hashtag_text.setForeground(new java.awt.Color(0, 0, 0));
        search_hashtag_text.setText("Hashtag");
        search_hashtag_text.setToolTipText("");
        search_hashtag_text.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(99, 167, 226)));
        search_hashtag_text.setCaretColor(new java.awt.Color(99, 137, 226));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(136, 136, 136)));
        jScrollPane1.setToolTipText("");
        jScrollPane1.setPreferredSize(new Dimension(584, 296)
        );

        resultPanel.setBackground(new java.awt.Color(255, 255, 255));
        resultPanel.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout resultPanelLayout = new javax.swing.GroupLayout(resultPanel);
        resultPanel.setLayout(resultPanelLayout);
        resultPanelLayout.setHorizontalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 584, Short.MAX_VALUE)
        );
        resultPanelLayout.setVerticalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(resultPanel);

        search_send_btn.setBackground(new java.awt.Color(255, 255, 255));
        search_send_btn.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        search_send_btn.setForeground(new java.awt.Color(0, 0, 0));
        search_send_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/selegram/send.png"))); // NOI18N
        search_send_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                search_send_btnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                search_send_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                search_send_btnMouseExited(evt);
            }
        });

        home_btn.setBackground(new java.awt.Color(255, 255, 255));
        home_btn.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        home_btn.setForeground(new java.awt.Color(0, 0, 0));
        home_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/selegram/favicon32.png"))); // NOI18N
        home_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                home_btnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(search_lbl1)
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(search_hashtag_text, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(search_username_text, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(search_username_btn)
                            .addComponent(search_hashtag_btn))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(search_send_btn)
                            .addComponent(search_addfrirend_btn))
                        .addContainerGap(28, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(home_btn)
                        .addContainerGap())))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(44, 44, 44)
                    .addComponent(search_lbl2)
                    .addContainerGap(594, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(search_username_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(search_username_btn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(search_lbl1)
                                .addComponent(search_hashtag_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(search_hashtag_btn)))
                    .addComponent(home_btn))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(34, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 216, Short.MAX_VALUE)
                        .addComponent(search_addfrirend_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(search_send_btn)
                        .addGap(56, 56, 56))))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addComponent(search_lbl2)
                    .addContainerGap(382, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void search_username_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_username_btnMouseEntered
        // TODO add your handling code here:
        search_username_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/search_hover.png")));
    }//GEN-LAST:event_search_username_btnMouseEntered

    private void search_username_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_username_btnMouseExited
        // TODO add your handling code here:
        search_username_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/search.png")));
    }//GEN-LAST:event_search_username_btnMouseExited

    private void search_hashtag_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_hashtag_btnMouseEntered
        // TODO add your handling code here:
        search_hashtag_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/search_hover.png")));
    }//GEN-LAST:event_search_hashtag_btnMouseEntered

    private void search_hashtag_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_hashtag_btnMouseExited
        // TODO add your handling code here:
        search_hashtag_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/search.png")));
    }//GEN-LAST:event_search_hashtag_btnMouseExited

    private void search_addfrirend_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_addfrirend_btnMouseEntered
        // TODO add your handling code here:
        search_addfrirend_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/add_friendh.png")));
    }//GEN-LAST:event_search_addfrirend_btnMouseEntered

    private void search_addfrirend_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_addfrirend_btnMouseExited
        // TODO add your handling code here:
        search_addfrirend_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/add_friend.png")));
    }//GEN-LAST:event_search_addfrirend_btnMouseExited

    private void search_send_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_send_btnMouseEntered
        // TODO add your handling code here:
        search_send_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/sendh.png")));
    }//GEN-LAST:event_search_send_btnMouseEntered

    private void search_send_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_send_btnMouseExited
        // TODO add your handling code here:
        search_send_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/send.png")));
    }//GEN-LAST:event_search_send_btnMouseExited

    private void search_addfrirend_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_addfrirend_btnMouseClicked
        // TODO add your handling code here:

        JRadioButton selected = getSelectedPerson();
        if (selected != null) {
            JSONObject obj = new JSONObject();
            obj.put("command", "add_friend").put("options", new JSONObject().put("username", mainFrame.user.getUsername()).put("friend_username", selected.getText()));
            String result = mainFrame.clientHandler.getAnswer(obj.toString());
            System.out.println(result);
            JSONObject res = new JSONObject(result);
            if (res.getString("status").equalsIgnoreCase("ok")) {
                mainFrame.switchPanels(this, new MessagePanel(mainFrame));
            }
        }
    }//GEN-LAST:event_search_addfrirend_btnMouseClicked

    private void home_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_home_btnMouseClicked
        // TODO add your handling code here:
        mainFrame.switchPanels(this, new MessagePanel(mainFrame));
    }//GEN-LAST:event_home_btnMouseClicked

    private void search_hashtag_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_hashtag_btnMouseClicked
        // TODO add your handling code here:
        type = "hashtag";
        search_addfrirend_btn.setVisible(false);
        search_send_btn.setVisible(false);
        hashtags = searchHashtags(search_hashtag_text.getText().trim());
    }//GEN-LAST:event_search_hashtag_btnMouseClicked

    private void search_username_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_username_btnMouseClicked
        // TODO add your handling code here:
        type = "friend";
        search_addfrirend_btn.setVisible(true);
        search_send_btn.setVisible(true);
        friends = searchPersons(search_username_text.getText().trim());
    }//GEN-LAST:event_search_username_btnMouseClicked

    private void search_send_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_send_btnMouseClicked
        // TODO add your handling code here:
        JRadioButton selected = getSelectedPerson();
        if (selected != null) {
            ///
            ///
            
            boolean flag = true;
            for(int i=0;i<mainFrame.user.getFriends().size();i++){
                if(mainFrame.user.getFriends().get(i).fusername.equalsIgnoreCase(selected.getText())){
                    flag = false;
                    break;
                }
            }
            if (flag) {
                JSONObject obj = new JSONObject();
                obj.put("command", "add_friend").put("options", new JSONObject().put("username", mainFrame.user.getUsername()).put("friend_username", selected.getText()));
                String result = mainFrame.clientHandler.getAnswer(obj.toString());
                System.out.println(result);
                JSONObject res = new JSONObject(result);
                if (res.getString("status").equalsIgnoreCase("ok")) {
                    obj = new JSONObject();
                    obj.put("command", "unfriend").put("options", new JSONObject().put("username", mainFrame.user.getUsername()).put("friend_username", selected.getText()));
                    result = mainFrame.clientHandler.getAnswer(obj.toString());
                    System.out.println(result);
                    mainFrame.switchPanels(this, new MessagePanel(mainFrame));
                }
            }

        }
    }//GEN-LAST:event_search_send_btnMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel home_btn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel resultPanel;
    private javax.swing.JLabel search_addfrirend_btn;
    private javax.swing.JLabel search_hashtag_btn;
    private javax.swing.JTextField search_hashtag_text;
    private javax.swing.JLabel search_lbl1;
    private javax.swing.JLabel search_lbl2;
    private javax.swing.JLabel search_send_btn;
    private javax.swing.JLabel search_username_btn;
    private javax.swing.JTextField search_username_text;
    // End of variables declaration//GEN-END:variables
}