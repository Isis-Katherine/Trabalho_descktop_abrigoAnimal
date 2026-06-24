package screen;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Date;
import data.Adoacao;
import data.Animal;
import data.Adotante;

public class DialogEditarAdoacao extends JDialog {

    private static final Color AMARELO        = new Color(251, 192, 45);
    private static final Color AMARELO_HOVER  = new Color(249, 168, 37);
    private static final Color FUNDO        = new Color(248, 250, 248);
    private static final Color BRANCO       = Color.WHITE;
    private static final Color CINZA_BORDA  = new Color(220, 220, 220);

    private Adoacao adocao;
    private Runnable refreshCallback;
    private JComboBox<String> comboAnimal;
    private JComboBox<String> comboAdotante;
    private JTextField campoData;
    private JCheckBox chkDevolvido;
    private JTextField campoDataDevolucao;


    public DialogEditarAdoacao(
            Window pai,
            Adoacao adocao,
            Runnable refreshCallback
    ) {

        super(pai, "Editar Adoção", ModalityType.APPLICATION_MODAL);
        this.adocao = adocao;
        this.refreshCallback = refreshCallback;

        setSize(520, 750);
        setLocationRelativeTo(pai);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FUNDO);

        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);
        add(criarBotoes(), BorderLayout.SOUTH);
    }

    // CABEÇALHO
    private JPanel criarCabecalho() {
        JPanel cab = new JPanel(new BorderLayout());
        cab.setBackground(AMARELO);
        cab.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel titulo = new JLabel("Editar Adoção");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(BRANCO);

        JLabel sub = new JLabel("Altere os dados da adoção");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(220, 240, 220));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(sub);

        cab.add(textos, BorderLayout.CENTER);
        return cab;
    }

    // FORMULÁRIO
    private JPanel criarFormulario() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(FUNDO);
        form.setBorder(new EmptyBorder(24, 28, 8, 28));

        String animalNome = adocao.getAnimal() != null ? adocao.getAnimal().getNome() : "";
        String adotanteNome = adocao.getAdotante() != null ? adocao.getAdotante().getNome() : "";
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String dataStr = adocao.getDataAdoacao() != null ? sdf.format(adocao.getDataAdoacao()) : "";

        comboAnimal = new JComboBox<>();
        comboAnimal.setEditable(true);

        for (Animal a : utils.SistemaDados.animais) {
            comboAnimal.addItem(a.getNome());
        }
        comboAnimal.setSelectedItem(animalNome);

        comboAdotante = new JComboBox<>();
        comboAdotante.setEditable(true);

        for (Adotante ad : utils.SistemaDados.adotantes) {
            comboAdotante.addItem(ad.getNome());
        }
        comboAdotante.setSelectedItem(adotanteNome);

        campoData = criarCampoTextoComponente(dataStr);
        chkDevolvido = new JCheckBox("Animal devolvido");

        campoDataDevolucao = criarCampoTextoComponente("");

        if (adocao.getDataDevolucao() != null) {

            campoDataDevolucao.setText(
                sdf.format(adocao.getDataDevolucao())
            );

            chkDevolvido.setSelected(true);
        }

        configurarAutoCompleteComboAnimal();
        configurarAutoCompleteComboAdotante();

        form.add(criarGrupoCombo(
            "Animal",
            comboAnimal
        ));

        form.add(Box.createVerticalStrut(20));

        form.add(criarGrupoCombo(
            "Adotante",
            comboAdotante
        ));
        
        form.add(Box.createVerticalStrut(20));
        form.add(criarGrupo("Data da adoção", campoData));
        form.add(Box.createVerticalStrut(20));
        form.add(chkDevolvido);
        form.add(Box.createVerticalStrut(20));
        form.add(criarGrupo(
            "Data da devolução",
            campoDataDevolucao
        ));

        return form;
    }

    private JTextField criarCampoTextoComponente(String valor) {
        JTextField campo = new JTextField(valor) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setColor(BRANCO);
                g2.fill(new RoundRectangle2D.Float(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    10,
                    10
                ));
                g2.setColor(CINZA_BORDA);
                g2.draw(new RoundRectangle2D.Float(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    10,
                    10
                ));
                g2.dispose();
                super.paintComponent(g);
            }
        };

        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(new EmptyBorder(10, 12, 10, 12));
        campo.setOpaque(false);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        return campo;
    }

    private JPanel criarGrupo(String rotulo, JTextField campo) {
        JPanel grupo = new JPanel();
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setOpaque(false);
        grupo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(rotulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        grupo.add(lbl);
        grupo.add(Box.createVerticalStrut(5));
        grupo.add(campo);

        return grupo;
    }

    // BARRA DE PESQUISA PERSONALISADA ---------

    private JPanel criarGrupoCombo(
        String rotulo,
        JComboBox<String> combo) {

    JPanel grupo = new JPanel();
    grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
    grupo.setOpaque(false);

    JLabel lbl = new JLabel(rotulo);
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));

    combo.setMaximumSize(
        new Dimension(Integer.MAX_VALUE, 40)
    );

    grupo.add(lbl);
    grupo.add(Box.createVerticalStrut(5));
    grupo.add(combo);

    return grupo;
}

    // BOTÕES
    private JPanel criarBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        painel.setBackground(FUNDO);
        painel.setBorder(new MatteBorder(
            1,
            0,
            0,
            0,
            CINZA_BORDA
        ));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancelar.setBackground(new Color(238, 238, 238));
        btnCancelar.setForeground(new Color(60, 60, 60));
        btnCancelar.setBorder(new CompoundBorder(
                new LineBorder(CINZA_BORDA, 1, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnSalvar = new JButton("Salvar Alterações") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setColor(
                    getModel().isRollover()
                    ? AMARELO_HOVER
                    : AMARELO
                );
                g2.fill(new RoundRectangle2D.Float(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    10,
                    10
                ));
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setForeground(BRANCO);
        btnSalvar.setContentAreaFilled(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorder(new EmptyBorder(10, 24, 10, 24));
        btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> {
            
            String strData = campoData.getText().trim();

            String nomeAnimal =
                comboAnimal.getSelectedItem() != null
                ? comboAnimal.getSelectedItem().toString().trim()
                : "";

            String nomeAdotante =
                comboAdotante.getSelectedItem() != null
                ? comboAdotante.getSelectedItem().toString().trim()
                : "";
                
            if (strData.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, especifique a data da adoção!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Find animal
            Animal newAnimal = null;
            for (Animal a : utils.SistemaDados.animais) {
                if (a.getNome().equalsIgnoreCase(nomeAnimal)) {
                    newAnimal = a;
                    break;
                }
            }
            if (newAnimal == null) {
                JOptionPane.showMessageDialog(this, "Animal \"" + nomeAnimal + "\" não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Find adopter
            Adotante newAdotante = null;
            for (Adotante ad : utils.SistemaDados.adotantes) {
                if (ad.getNome().equalsIgnoreCase(nomeAdotante) || ad.getCpf().equalsIgnoreCase(nomeAdotante)) {
                    newAdotante = ad;
                    break;
                }
            }
            if (newAdotante == null) {
                JOptionPane.showMessageDialog(this, "Adotante \"" + nomeAdotante + "\" não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse Date
            Date dataAdoacao;
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                try {
                    dataAdoacao = sdf.parse(strData);
                } catch (Exception ex) {
                    java.text.SimpleDateFormat sdfFull = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
                    dataAdoacao = sdfFull.parse(strData);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Data inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update animal status
            if (adocao.getAnimal() != null && !adocao.getAnimal().equals(newAnimal)) {
                adocao.getAnimal().setaAotado(false);
            }
            newAnimal.setaAotado(true);

            adocao.setAnimal(newAnimal);
            adocao.setAdotante(newAdotante);
            adocao.setDataAdoacao(dataAdoacao);

            if (chkDevolvido.isSelected()) {

                try {

                    java.text.SimpleDateFormat sdf =
                        new java.text.SimpleDateFormat("dd/MM/yyyy");

                    Date dataDevolucao =
                        sdf.parse(campoDataDevolucao.getText());

                    adocao.setDataDevolucao(dataDevolucao);

                    newAnimal.setaAotado(false);

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Data de devolução inválida!"
                    );

                    return;
                }

            } else {

                adocao.setDataDevolucao(null);

                newAnimal.setaAotado(true);
            }

            utils.SistemaDados.save();

            JOptionPane.showMessageDialog(
                this,
                "Adoção alterada com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
            );

            if (refreshCallback != null) {
                refreshCallback.run();
            }
            dispose();
        });

        painel.add(btnCancelar);
        painel.add(btnSalvar);

        return painel;
    }

    private void configurarAutoCompleteComboAdotante() {

        JTextField editor =
            (JTextField) comboAdotante.getEditor().getEditorComponent();

        editor.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {

                SwingUtilities.invokeLater(() -> {

                    String texto = editor.getText();

                    DefaultComboBoxModel<String> model =
                        new DefaultComboBoxModel<>();

                    for (Adotante ad : utils.SistemaDados.adotantes) {

                        if (ad.getNome().toLowerCase()
                                .contains(texto.toLowerCase())) {

                            model.addElement(ad.getNome());
                        }
                    }

                    comboAdotante.setModel(model);
                    comboAdotante.setSelectedItem(texto);
                    editor.setText(texto);

                    if (model.getSize() > 0) {
                        comboAdotante.showPopup();
                    }
                });
            }
        });
    }

private void configurarAutoCompleteComboAnimal() {

    JTextField editor =
        (JTextField) comboAnimal.getEditor().getEditorComponent();

    editor.addKeyListener(new java.awt.event.KeyAdapter() {

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {

            SwingUtilities.invokeLater(() -> {

                String texto = editor.getText();

                DefaultComboBoxModel<String> model =
                    new DefaultComboBoxModel<>();

                for (Animal a : utils.SistemaDados.animais) {

                    if (a.getNome().toLowerCase()
                            .contains(texto.toLowerCase())) {

                        model.addElement(a.getNome());
                    }
                }

                comboAnimal.setModel(model);
                comboAnimal.setSelectedItem(texto);
                editor.setText(texto);

                if (model.getSize() > 0) {
                    comboAnimal.showPopup();
                }
            });
        }
    });
}
}