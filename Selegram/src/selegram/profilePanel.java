/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selegram;

import java.util.Base64;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.json.JSONObject;

/**
 *
 * @author SKings
 */
public class profilePanel extends javax.swing.JPanel {

    /**
     * Creates new form profilePanel
     */
    MainFrame mainFrame;
    String proUsername;

    public profilePanel(MainFrame mainFrame, String proUsername) {
        initComponents();
        this.mainFrame = mainFrame;
        this.proUsername = proUsername;
        if (this.proUsername.equals(mainFrame.user.getUsername())) {
            myPro();
        } else {
            otherPro();
        }
    }

    /**
     * programmer functions
     */
    public void myPro() {
        profile_username_text.setText(proUsername);
        profile_pass_text.setText(mainFrame.user.getPassword());
        
//        baraye test register budan bud
//        mainFrame.user.setRegistered(true);

        if (!mainFrame.user.isRegistered()) {
            create_channel_btn.setVisible(false);
            create_group_btn.setVisible(false);
            home_btn.setVisible(false);
            panel_sync_btn.setText("Register");
        } else {
            makeProfile();
        }
        // make jtext field editable
        profile_name_text.setEditable(true);
        profile_bday_text.setEditable(true);
        profile_email_text.setEditable(true);
        profile_bio_text.setEditable(true);
        profile_phone_text.setEditable(true);
        profile_pass_text.setEditable(true);
    }

    public void otherPro() {
        profile_username_text.setText(proUsername);
        // use net to make profile
        makeProfile();
        // remove some of the buttons
        profile_lbl7.setVisible(false);
        profile_pass_text.setVisible(false);
        create_channel_btn.setVisible(false);
        create_group_btn.setVisible(false);
        panel_sync_btn.setVisible(false);
    }

    /**
     *
     * @param name
     * @param username
     * @param bday
     * @param email
     * @param bio
     * @param phone
     * @param password
     * @param pic
     * @return 1 if succeed 0 if cant sync
     */
    public int sendProfile(String name, String username, String bday, String email, String bio, String phone, String password, String pic) {
        System.out.println("+++++++++++++++PIC IN BTN : "+pic );
        JSONObject obj = new JSONObject();
        obj.put("command", "profile_edit").put("options",
                new JSONObject().put("user_name", username)
                .put("first_name", name)
                .put("last_name", "")
                .put("phone", phone)
                .put("birth_day", bday)
                .put("email", email)
                .put("bio", bio)
                .put("img", pic)
                .put("password", password)
        );
        String result = mainFrame.clientHandler.getAnswer(obj.toString());
        System.out.println(result);
        // inja bayad edit profile dorost javab bede age dorost bud bayad return 1 beshe va khat zir ke mige register shude age eshtebah bud faqat reurn 0
        //mainFrame.user.setRegistered(true);
        //done √
        JSONObject res = new JSONObject(result);
        if (res.get("status").toString().equalsIgnoreCase("ok")) {
            return 1;
        }
        return 0;
    }

    /**
     * sakhtan profile ba estefade az username hatman test kon bade in ke edit
     * profile dorost shud
     */
    public void makeProfile() {
        System.out.println("here");
        JSONObject obj = new JSONObject();
        obj.put("command", "get_profile").put("options", new JSONObject().put("username", proUsername));
        String result = mainFrame.clientHandler.getAnswer(obj.toString());
        System.out.println(result);
        JSONObject res = new JSONObject(result);
        if (res.get("status").toString().equalsIgnoreCase("err")) {
            JOptionPane.showMessageDialog(mainFrame, "user not found");
        } else {
            System.out.println(res.get("payload"));
            res = new JSONObject(res.get("payload").toString());
            profile_bday_text.setText(res.get("birth_day").toString());
            profile_email_text.setText(res.getString("email"));
            profile_bio_text.setText(res.getString("bio"));
            profile_phone_text.setText(res.getString("phone"));
            profile_name_text.setText(res.getString("first_name"));
            profile_username_text.setText(res.getString("username"));
            profile_pass_text.setText(res.getString("password"));
            
            String pic64 = res.getString("img");
            //System.out.println("here : "+pic64);
            byte[] bimg = Base64.getDecoder().decode(pic64);
            //System.out.println(""+bimg);
            profile_pic_lbl.setIcon(new ImageIcon(bimg));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        profile_pic_lbl = new javax.swing.JLabel();
        profile_lbl1 = new javax.swing.JLabel();
        profile_lbl2 = new javax.swing.JLabel();
        profile_lbl3 = new javax.swing.JLabel();
        profile_lbl4 = new javax.swing.JLabel();
        profile_lbl5 = new javax.swing.JLabel();
        profile_lbl6 = new javax.swing.JLabel();
        profile_lbl7 = new javax.swing.JLabel();
        profile_name_text = new javax.swing.JTextField();
        profile_username_text = new javax.swing.JTextField();
        profile_email_text = new javax.swing.JTextField();
        profile_phone_text = new javax.swing.JTextField();
        profile_pass_text = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        profile_bio_text = new javax.swing.JTextArea();
        create_channel_btn = new javax.swing.JButton();
        panel_sync_btn = new javax.swing.JButton();
        create_group_btn = new javax.swing.JButton();
        home_btn = new javax.swing.JLabel();
        profile_bday_text = new javax.swing.JFormattedTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(700, 430));

        profile_pic_lbl.setBackground(new java.awt.Color(255, 255, 255));
        profile_pic_lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/selegram/profile.png"))); // NOI18N
        profile_pic_lbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                profile_pic_lblMouseClicked(evt);
            }
        });

        profile_lbl1.setBackground(new java.awt.Color(255, 255, 255));
        profile_lbl1.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        profile_lbl1.setForeground(new java.awt.Color(0, 0, 0));
        profile_lbl1.setText("Name:");

        profile_lbl2.setBackground(new java.awt.Color(255, 255, 255));
        profile_lbl2.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        profile_lbl2.setForeground(new java.awt.Color(0, 0, 0));
        profile_lbl2.setText("Birthday:");

        profile_lbl3.setBackground(new java.awt.Color(255, 255, 255));
        profile_lbl3.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        profile_lbl3.setForeground(new java.awt.Color(0, 0, 0));
        profile_lbl3.setText("Username:");

        profile_lbl4.setBackground(new java.awt.Color(255, 255, 255));
        profile_lbl4.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        profile_lbl4.setForeground(new java.awt.Color(0, 0, 0));
        profile_lbl4.setText("Email:");

        profile_lbl5.setBackground(new java.awt.Color(255, 255, 255));
        profile_lbl5.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        profile_lbl5.setForeground(new java.awt.Color(0, 0, 0));
        profile_lbl5.setText("Biography:");

        profile_lbl6.setBackground(new java.awt.Color(255, 255, 255));
        profile_lbl6.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        profile_lbl6.setForeground(new java.awt.Color(0, 0, 0));
        profile_lbl6.setText("Phone number:");

        profile_lbl7.setBackground(new java.awt.Color(255, 255, 255));
        profile_lbl7.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        profile_lbl7.setForeground(new java.awt.Color(0, 0, 0));
        profile_lbl7.setText("password:");

        profile_name_text.setEditable(false);
        profile_name_text.setBackground(new java.awt.Color(255, 255, 255));
        profile_name_text.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        profile_name_text.setForeground(new java.awt.Color(0, 0, 0));
        profile_name_text.setText("Name");
        profile_name_text.setToolTipText("name");
        profile_name_text.setBorder(null);
        profile_name_text.setCaretColor(new java.awt.Color(99, 137, 226));

        profile_username_text.setEditable(false);
        profile_username_text.setBackground(new java.awt.Color(255, 255, 255));
        profile_username_text.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        profile_username_text.setForeground(new java.awt.Color(0, 0, 0));
        profile_username_text.setText("Username");
        profile_username_text.setToolTipText("username");
        profile_username_text.setBorder(null);
        profile_username_text.setCaretColor(new java.awt.Color(99, 137, 226));

        profile_email_text.setEditable(false);
        profile_email_text.setBackground(new java.awt.Color(255, 255, 255));
        profile_email_text.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        profile_email_text.setForeground(new java.awt.Color(0, 0, 0));
        profile_email_text.setText("google@google.com");
        profile_email_text.setToolTipText("email");
        profile_email_text.setBorder(null);
        profile_email_text.setCaretColor(new java.awt.Color(99, 137, 226));

        profile_phone_text.setEditable(false);
        profile_phone_text.setBackground(new java.awt.Color(255, 255, 255));
        profile_phone_text.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        profile_phone_text.setForeground(new java.awt.Color(0, 0, 0));
        profile_phone_text.setText("22122122");
        profile_phone_text.setToolTipText("phone");
        profile_phone_text.setBorder(null);
        profile_phone_text.setCaretColor(new java.awt.Color(99, 137, 226));

        profile_pass_text.setEditable(false);
        profile_pass_text.setBackground(new java.awt.Color(255, 255, 255));
        profile_pass_text.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        profile_pass_text.setForeground(new java.awt.Color(0, 0, 0));
        profile_pass_text.setText("mypass");
        profile_pass_text.setToolTipText("password");
        profile_pass_text.setBorder(null);
        profile_pass_text.setCaretColor(new java.awt.Color(99, 137, 226));

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(null);

        profile_bio_text.setEditable(false);
        profile_bio_text.setBackground(new java.awt.Color(255, 255, 255));
        profile_bio_text.setColumns(20);
        profile_bio_text.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        profile_bio_text.setForeground(new java.awt.Color(0, 0, 0));
        profile_bio_text.setRows(5);
        profile_bio_text.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(99, 137, 228), 2));
        jScrollPane2.setViewportView(profile_bio_text);

        create_channel_btn.setBackground(new java.awt.Color(47, 169, 226));
        create_channel_btn.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        create_channel_btn.setForeground(new java.awt.Color(255, 255, 255));
        create_channel_btn.setText("Create Channel");
        create_channel_btn.setBorder(null);
        create_channel_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                create_channel_btnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                create_channel_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                create_channel_btnMouseExited(evt);
            }
        });

        panel_sync_btn.setBackground(new java.awt.Color(47, 169, 226));
        panel_sync_btn.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        panel_sync_btn.setForeground(new java.awt.Color(255, 255, 255));
        panel_sync_btn.setText("Sync");
        panel_sync_btn.setBorder(null);
        panel_sync_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel_sync_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel_sync_btnMouseExited(evt);
            }
        });
        panel_sync_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel_sync_btnActionPerformed(evt);
            }
        });

        create_group_btn.setBackground(new java.awt.Color(47, 169, 226));
        create_group_btn.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        create_group_btn.setForeground(new java.awt.Color(255, 255, 255));
        create_group_btn.setText("Create group");
        create_group_btn.setBorder(null);
        create_group_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                create_group_btnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                create_group_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                create_group_btnMouseExited(evt);
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

        profile_bday_text.setEditable(false);
        profile_bday_text.setBackground(new java.awt.Color(255, 255, 255));
        profile_bday_text.setBorder(null);
        profile_bday_text.setForeground(new java.awt.Color(0, 0, 0));
        profile_bday_text.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        profile_bday_text.setText("Jun 27, 2016");
        profile_bday_text.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(profile_pic_lbl))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(create_group_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(create_channel_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panel_sync_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(profile_lbl1)
                            .addComponent(profile_lbl3)
                            .addComponent(profile_lbl2)
                            .addComponent(profile_lbl4)
                            .addComponent(profile_lbl5))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(profile_email_text, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                                .addComponent(profile_username_text)
                                .addComponent(profile_name_text))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(profile_bday_text, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(profile_lbl6, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(profile_lbl7, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(profile_phone_text, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(profile_pass_text, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                .addComponent(home_btn)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(profile_lbl1)
                                    .addComponent(profile_name_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(profile_lbl3)
                                    .addComponent(profile_username_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(home_btn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(profile_lbl2)
                            .addComponent(profile_bday_text, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(profile_pic_lbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(profile_lbl4)
                    .addComponent(profile_email_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(profile_lbl5)
                            .addComponent(create_channel_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(create_group_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profile_lbl6)
                    .addComponent(profile_phone_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profile_lbl7)
                    .addComponent(profile_pass_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(panel_sync_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void create_channel_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_channel_btnMouseEntered
        // TODO add your handling code here:
        create_channel_btn.setBackground(new Color(37, 159, 216));
    }//GEN-LAST:event_create_channel_btnMouseEntered

    private void create_channel_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_channel_btnMouseExited
        // TODO add your handling code here:
        create_channel_btn.setBackground(new Color(47, 169, 226));
    }//GEN-LAST:event_create_channel_btnMouseExited

    private void panel_sync_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_sync_btnMouseEntered
        // TODO add your handling code here:
        panel_sync_btn.setBackground(new Color(37, 159, 216));
    }//GEN-LAST:event_panel_sync_btnMouseEntered

    private void panel_sync_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_sync_btnMouseExited
        // TODO add your handling code here:
        panel_sync_btn.setBackground(new Color(47, 169, 226));
    }//GEN-LAST:event_panel_sync_btnMouseExited

    private void create_group_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_group_btnMouseEntered
        // TODO add your handling code here:
        create_group_btn.setBackground(new Color(37, 159, 216));
    }//GEN-LAST:event_create_group_btnMouseEntered

    private void create_group_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_group_btnMouseExited
        // TODO add your handling code here:
        create_group_btn.setBackground(new Color(47, 169, 226));
    }//GEN-LAST:event_create_group_btnMouseExited

    private void panel_sync_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_panel_sync_btnActionPerformed
        // TODO add your handling code here:
//        if ((!profile_name_text.getText().trim().isEmpty()) && (!profile_username_text.getText().trim().isEmpty())
//                && (!profile_email_text.getText().trim().isEmpty()) && (!profile_phone_text.getText().trim().isEmpty())
//                && (!profile_bday_text.getText().trim().isEmpty()) && (!profile_bio_text.getText().trim().isEmpty())
//                && (!profile_pass_text.getText().isEmpty())) {
        if ((!profile_pass_text.getText().isEmpty()) && (!profile_username_text.getText().trim().isEmpty())) {

            Icon icon = profile_pic_lbl.getIcon();
            BufferedImage image = new BufferedImage(icon.getIconWidth(),
                    icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "png", b);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "bad profile image!!");
            }
            byte[] imageByteArray = b.toByteArray();
            System.out.println(Base64.getEncoder().encodeToString(imageByteArray));
            String pic = Base64.getEncoder().encodeToString(imageByteArray);
            System.out.println("--------------PIC IN BTN : "+pic);
            System.out.println(profile_pass_text.getText());
            int state = sendProfile(profile_name_text.getText().trim(), profile_username_text.getText(),
                    profile_bday_text.getText().trim(), profile_email_text.getText().trim(),
                    profile_bio_text.getText().trim(), profile_phone_text.getText().trim(),
                    profile_pass_text.getText(),pic );

            if (state == 1) {
                mainFrame.user.setRegistered(true); 
                mainFrame.switchPanels(this, new MessagePanel(mainFrame));
            } else {
                JOptionPane.showMessageDialog(null, "cant send data to server!!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "password  field is empty!!");
        }
    }//GEN-LAST:event_panel_sync_btnActionPerformed

    private void home_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_home_btnMouseClicked
        // TODO add your handling code here:
        mainFrame.switchPanels(this, new MessagePanel(mainFrame));
    }//GEN-LAST:event_home_btnMouseClicked

    private void create_channel_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_channel_btnMouseClicked
        // TODO add your handling code here:
        mainFrame.switchPanels(this, new CreateGroupPanel(mainFrame, "channel"));
    }//GEN-LAST:event_create_channel_btnMouseClicked

    private void create_group_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_group_btnMouseClicked
        // TODO add your handling code here:
        mainFrame.switchPanels(this, new CreateGroupPanel(mainFrame, "group"));
    }//GEN-LAST:event_create_group_btnMouseClicked

    private void profile_pic_lblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_profile_pic_lblMouseClicked
        // TODO add your handling code here:
        if (proUsername.equals(mainFrame.user.getUsername())) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Images", "jpg", "png", "bmp");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(mainFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose to open this file: "
                        + chooser.getSelectedFile().getName());
                BufferedImage bImage = null;
                try {
                    // read image
                    bImage = ImageIO.read(chooser.getSelectedFile());
                    // resize image
                    BufferedImage resizedImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = resizedImage.createGraphics();
                    g.drawImage(bImage, 0, 0, 64, 64, null);
                    g.dispose();
                    bImage = resizedImage;
                    //image to byte array
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bImage, "png", baos);
                    baos.flush();
                    byte[] imageByteArray = baos.toByteArray();
                    baos.close();
                    // byte array to base 64
                    
                    mainFrame.user.setPic64(Base64.getEncoder().encodeToString(imageByteArray));
                    profile_pic_lbl.setIcon(new ImageIcon(imageByteArray));

                } catch (IOException ex) {
                    Logger.getLogger(profilePanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }//GEN-LAST:event_profile_pic_lblMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton create_channel_btn;
    private javax.swing.JButton create_group_btn;
    private javax.swing.JLabel home_btn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton panel_sync_btn;
    private javax.swing.JFormattedTextField profile_bday_text;
    private javax.swing.JTextArea profile_bio_text;
    private javax.swing.JTextField profile_email_text;
    private javax.swing.JLabel profile_lbl1;
    private javax.swing.JLabel profile_lbl2;
    private javax.swing.JLabel profile_lbl3;
    private javax.swing.JLabel profile_lbl4;
    private javax.swing.JLabel profile_lbl5;
    private javax.swing.JLabel profile_lbl6;
    private javax.swing.JLabel profile_lbl7;
    private javax.swing.JTextField profile_name_text;
    private javax.swing.JTextField profile_pass_text;
    private javax.swing.JTextField profile_phone_text;
    private javax.swing.JLabel profile_pic_lbl;
    private javax.swing.JTextField profile_username_text;
    // End of variables declaration//GEN-END:variables
}
