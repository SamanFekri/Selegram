/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selegram;

import java.awt.Color;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.json.JSONObject;

/**
 *
 * @author SKings
 */
public class loginPanel extends javax.swing.JPanel {

    /**
     * Creates new form loginPanel
     */
    MainFrame mainFrame;

    public loginPanel(MainFrame mainFrame) {
        initComponents();
        this.mainFrame = mainFrame;
    }

    /**
     * Programmer Functions
     */
    /**
     * @return 0 if username or password is wrong or does not exist else
     * @return 1 if user profile is incompelete
     * @return 2 if user profile is compelete
     * @return 3 if user morethan 10 times reported
     */
    public int loginRegistered(String username, String password) {
        JSONObject obj = new JSONObject();
        obj.put("command", "login").put("options", new JSONObject().put("username", username).put("password", password));
        String result = mainFrame.clientHandler.getAnswer(obj.toString());
        System.out.println(result);
        JSONObject res = new JSONObject(result);
        if (res.get("status").toString().equalsIgnoreCase("err")) {
            return 0;
        } else {
            System.out.println(res.get("payload"));
            res = new JSONObject(res.get("payload").toString());
            if (res.get("reported_date").toString().equals("")) {
                System.out.println(res);
                if (res.getBoolean("registered")) {
                    return 2;
                } else {
                    return 1;
                }
            } else {
                return 3;
            }
        }

    }

    /**
     *
     * @param username
     * @param password
     * @return 0 if username exist 1 if not exist
     */
    public int registering(String username, String password) {
        JSONObject obj = new JSONObject();
        obj.put("command", "register").put("options", new JSONObject().put("username", username).put("password", password));
        String result = mainFrame.clientHandler.getAnswer(obj.toString());
        System.out.println(result);
        JSONObject res = new JSONObject(result);
        if (res.get("status").toString().equalsIgnoreCase("err")) {
            return 0;
        } else {
            return 1;
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

        login_user_label = new javax.swing.JLabel();
        login_user_text = new javax.swing.JTextField();
        login_paas_label = new javax.swing.JLabel();
        login_pass_text = new javax.swing.JTextField();
        login_login_btn = new javax.swing.JButton();
        login_register_btn = new javax.swing.JButton();
        login_err_label = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N

        login_user_label.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        login_user_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        login_user_label.setText("Username");

        login_user_text.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        login_user_text.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(47, 169, 226), 2));
        login_user_text.setCaretColor(new java.awt.Color(47, 163, 226));
        login_user_text.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        login_user_text.setMargin(new java.awt.Insets(2, 10, 2, 10));

        login_paas_label.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        login_paas_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        login_paas_label.setText("Password");

        login_pass_text.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        login_pass_text.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(47, 169, 226), 2));
        login_pass_text.setCaretColor(new java.awt.Color(47, 163, 226));
        login_pass_text.setMargin(new java.awt.Insets(2, 10, 2, 10));
        login_pass_text.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                login_pass_textActionPerformed(evt);
            }
        });

        login_login_btn.setBackground(new java.awt.Color(47, 169, 226));
        login_login_btn.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        login_login_btn.setForeground(new java.awt.Color(255, 255, 255));
        login_login_btn.setText("Login");
        login_login_btn.setBorder(null);
        login_login_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                login_login_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                login_login_btnMouseExited(evt);
            }
        });
        login_login_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                login_login_btnActionPerformed(evt);
            }
        });

        login_register_btn.setBackground(new java.awt.Color(47, 169, 226));
        login_register_btn.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        login_register_btn.setForeground(new java.awt.Color(255, 255, 255));
        login_register_btn.setText("Register");
        login_register_btn.setBorder(null);
        login_register_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                login_register_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                login_register_btnMouseExited(evt);
            }
        });
        login_register_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                login_register_btnActionPerformed(evt);
            }
        });

        login_err_label.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        login_err_label.setForeground(new java.awt.Color(255, 51, 51));
        login_err_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(225, 225, 225)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(login_user_label, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(login_pass_text, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(login_user_text, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(login_login_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(login_register_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(login_err_label, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(login_paas_label, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(220, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(login_user_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(login_user_text, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(login_paas_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(login_pass_text, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(login_login_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(login_register_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(login_err_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void login_pass_textActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_login_pass_textActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_login_pass_textActionPerformed

    private void login_login_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_login_login_btnMouseEntered
        // TODO add your handling code here:
        login_login_btn.setBackground(new Color(37, 159, 216));
    }//GEN-LAST:event_login_login_btnMouseEntered

    private void login_login_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_login_login_btnMouseExited
        // TODO add your handling code here:
        login_login_btn.setBackground(new Color(47, 169, 226));
    }//GEN-LAST:event_login_login_btnMouseExited

    private void login_register_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_login_register_btnMouseEntered
        // TODO add your handling code here:
        login_register_btn.setBackground(new Color(37, 159, 216));
    }//GEN-LAST:event_login_register_btnMouseEntered

    private void login_register_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_login_register_btnMouseExited
        // TODO add your handling code here:
        login_register_btn.setBackground(new Color(47, 169, 226));
    }//GEN-LAST:event_login_register_btnMouseExited

    private void login_register_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_login_register_btnActionPerformed
        // TODO add your handling code here:

        if (login_user_text.getText().trim().isEmpty()) {
            login_err_label.setText("Username is Empty");
        } else if (login_pass_text.getText().isEmpty()) {
            login_err_label.setText("Password is Empty");
        } else {
            int state = registering(login_user_text.getText().trim(), login_pass_text.getText());
            if (state == 0) {
                login_err_label.setText("Username already exist");
            } else {
                mainFrame.user = new User(login_user_text.getText().trim());
                mainFrame.user.setPassword(login_pass_text.getText());
                mainFrame.user.setRegistered(false);
                mainFrame.switchPanels(this, new profilePanel(mainFrame, mainFrame.user.getUsername()));
            }
        }
    }//GEN-LAST:event_login_register_btnActionPerformed

    private void login_login_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_login_login_btnActionPerformed
        // TODO add your handling code here:
        if (login_user_text.getText().trim().isEmpty()) {
            login_err_label.setText("Username is Empty");
        } else if (login_pass_text.getText().isEmpty()) {
            login_err_label.setText("Password is Empty");
        } else {
            int state = loginRegistered(login_user_text.getText().trim(), login_pass_text.getText());
            switch (state) {
                case 0:
                    login_err_label.setText("Wrong username or password");
                    break;
                case 1:
                    mainFrame.user = new User(login_user_text.getText().trim());
                    mainFrame.user.setPassword(login_pass_text.getText());
                    mainFrame.user.setRegistered(false);
                    mainFrame.switchPanels(this, new profilePanel(mainFrame, mainFrame.user.getUsername()));
                    break;
                case 2:
                    mainFrame.user = new User(login_user_text.getText().trim());
                    mainFrame.user.setPassword(login_pass_text.getText());
                    mainFrame.user.setRegistered(true);
                    mainFrame.switchPanels(this, new MessagePanel(mainFrame));
                    break;
                case 3:
                    login_err_label.setText("Sorry you are reported");
                    break;
            }
        }
    }//GEN-LAST:event_login_login_btnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel login_err_label;
    private javax.swing.JButton login_login_btn;
    private javax.swing.JLabel login_paas_label;
    private javax.swing.JTextField login_pass_text;
    private javax.swing.JButton login_register_btn;
    private javax.swing.JLabel login_user_label;
    private javax.swing.JTextField login_user_text;
    // End of variables declaration//GEN-END:variables
}
