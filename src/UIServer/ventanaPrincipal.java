package UIServer;

import Log.Configuracion;
import java.net.Socket;
import java.util.ArrayList;
import servidor.cliente;
import servidor.control.conexionControl;
import servidor.coreMensajeria;
import servidor.mensajeria;

/**
 * ventana principal del servidor, que contendrá 
 * el número total de usuarios y el log de mensajes enviados
 * @author Tautvydas Bagocius
 */
public class ventanaPrincipal extends javax.swing.JFrame {
    private conexionControl control;
    /**
     * Creates new form ventanaPrincipal
     */
    public ventanaPrincipal() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("VENTANA DEL SERVIDOR DE CHAT");
        iniciarServidor();
        jTextFieldNumeroConexiones.setText("NÚMERO DE CONEXIONES ACTUALES: 0");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextFieldNumeroConexiones = new javax.swing.JTextField();
        jButtonSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaChatGeneral = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jTextFieldNumeroConexiones.setEditable(false);

        jButtonSalir.setText("SALIR");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });

        jTextAreaChatGeneral.setEditable(false);
        jTextAreaChatGeneral.setColumns(20);
        jTextAreaChatGeneral.setRows(5);
        jScrollPane1.setViewportView(jTextAreaChatGeneral);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
                    .addComponent(jTextFieldNumeroConexiones))
                .addGap(18, 18, 18)
                .addComponent(jButtonSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldNumeroConexiones)
                    .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        System.exit(-1);
    }//GEN-LAST:event_jButtonSalirActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ventanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ventanaPrincipal().setVisible(true);
            }
        });
    }
    /**
     * Método principla que arranca el servidor
     * 1-Instanciamos el objeto configuración que contiene toda la configuración necesaria para que funcione
     * 2-Instanciamos la lista de clientes que serán hilos
     * 3-Instanciamos un array de nombreClientes
     * 4-Instanciamos el objeto mensajeria que es el encargado de trabajar con los mensajes recibidos y realizar
     * un control de máximo de mensajes a guardar
     * 5-Instanciamos el coreDeControl que es el encargado de enviar la lista de clientes que estan conectados actualmente
     * 6-Instanciamos el coreMensajeria que es el encargado de lanzar el puerto de escucha para recibir e enviar los mensajes
     * de cada cliente
     */
    private void iniciarServidor(){
        Configuracion configuracion = new Configuracion();
        ArrayList<cliente> hilos = new ArrayList<>();
        ArrayList<String> nombreClientes = new ArrayList<>();
        ArrayList <Socket> clienControls = new ArrayList<>();
        mensajeria mensaje = new mensajeria(jTextAreaChatGeneral,hilos,Integer.valueOf(configuracion.tambuffer));
        control=new conexionControl(9900,clienControls, nombreClientes);
        control.start();
        new coreMensajeria(Integer.valueOf(configuracion.puertom),hilos,nombreClientes,mensaje,jTextFieldNumeroConexiones,jTextAreaChatGeneral,Integer.valueOf(configuracion.numuser),control).start();
        
    }
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaChatGeneral;
    private javax.swing.JTextField jTextFieldNumeroConexiones;
    // End of variables declaration//GEN-END:variables
}
