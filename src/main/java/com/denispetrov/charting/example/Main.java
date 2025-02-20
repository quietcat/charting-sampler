package com.denispetrov.charting.example;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.denispetrov.charting.example.drawable.ExampleModelDraggableRectLayer;
import com.denispetrov.charting.example.drawable.ExampleModelLabelLayer;
import com.denispetrov.charting.example.drawable.ExampleModelRectLayer;
import com.denispetrov.charting.example.drawable.ViewportXAxisLayer;
import com.denispetrov.charting.example.drawable.ViewportYAxisLayer;
import com.denispetrov.charting.example.drawable.ViewportZeroMarkLayer;
import com.denispetrov.charting.example.model.ExampleModel;
import com.denispetrov.charting.example.model.Label;
import com.denispetrov.charting.layer.drawable.ViewportBackgroundLayer;
import com.denispetrov.charting.layer.service.ClickerServiceLayer;
import com.denispetrov.charting.layer.service.DraggerServiceLayer;
import com.denispetrov.charting.layer.service.PanServiceLayer;
import com.denispetrov.charting.layer.service.TrackerServiceLayer;
import com.denispetrov.charting.layer.service.ZoomServiceLayer;
import com.denispetrov.charting.model.AxisRange;
import com.denispetrov.charting.model.FRectangle;
import com.denispetrov.charting.view.ModelAwareView;
import com.denispetrov.charting.view.ViewContext;

public class Main {

    protected Shell shell;
    private Canvas zoomingPageCanvas;
    private ModelAwareView<ExampleModel> view;
    private ZoomServiceLayer zoomLayer;

    private void run() {
        Display display = Display.getDefault();
        shell = new Shell();
        shell.setLayout(new FillLayout(SWT.HORIZONTAL));
        createContents();
        shell.open();
        shell.layout();

        createView();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {

        TabFolder tabFolder = new TabFolder(shell, SWT.NONE);

        TabItem tbtmZooming = new TabItem(tabFolder, SWT.NONE);
        tbtmZooming.setText("Zooming");

        Composite zoomingPage = new Composite(tabFolder, SWT.NONE);
        tbtmZooming.setControl(zoomingPage);
        zoomingPage.setLayout(new GridLayout(1, false));
        zoomingPageCanvas = new Canvas(zoomingPage, SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND);
        zoomingPageCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        Composite zoomingPageControls = new Composite(zoomingPage, SWT.NONE);
        zoomingPageControls.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        zoomingPageControls.setLayout(new GridLayout(2, false));

        Group grpXAxisRange = new Group(zoomingPageControls, SWT.NONE);
        grpXAxisRange.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        grpXAxisRange.setText("X Axis Range");
        grpXAxisRange.setLayout(new FillLayout(SWT.VERTICAL));

        Button btnXFullRange = new Button(grpXAxisRange, SWT.RADIO);
        btnXFullRange.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                view.getViewContext().setXAxisRange(AxisRange.FULL);
                view.contextUpdated();
            }
        });
        btnXFullRange.setSelection(true);
        btnXFullRange.setText("Full");

        Button btnXPositiveRange = new Button(grpXAxisRange, SWT.RADIO);
        btnXPositiveRange.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                view.getViewContext().setXAxisRange(AxisRange.POSITIVE_ONLY);
                view.getViewContext().validateBaseX();
                view.contextUpdated();
            }
        });
        btnXPositiveRange.setText("Positive");

        Button btnXNegativeRange = new Button(grpXAxisRange, SWT.RADIO);
        btnXNegativeRange.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                view.getViewContext().setXAxisRange(AxisRange.NEGATIVE_ONLY);
                view.getViewContext().validateBaseX();
                view.contextUpdated();
            }
        });
        btnXNegativeRange.setText("Negative");

        Group grpYAxisRange = new Group(zoomingPageControls, SWT.NONE);
        grpYAxisRange.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        grpYAxisRange.setText("Y Axis Range");
        grpYAxisRange.setLayout(new FillLayout(SWT.VERTICAL));

        Button btnYFullRange = new Button(grpYAxisRange, SWT.RADIO);
        btnYFullRange.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                view.getViewContext().setYAxisRange(AxisRange.FULL);
                view.contextUpdated();
            }
        });
        btnYFullRange.setSelection(true);
        btnYFullRange.setText("Full");

        Button btnYPositiveRange = new Button(grpYAxisRange, SWT.RADIO);
        btnYPositiveRange.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                view.getViewContext().setYAxisRange(AxisRange.POSITIVE_ONLY);
                view.getViewContext().validateBaseY();
                view.contextUpdated();
            }
        });
        btnYPositiveRange.setText("Positive");

        Button btnYNegativeRange = new Button(grpYAxisRange, SWT.RADIO);
        btnYNegativeRange.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                view.getViewContext().setYAxisRange(AxisRange.NEGATIVE_ONLY);
                view.getViewContext().validateBaseY();
                view.contextUpdated();
            }
        });
        btnYNegativeRange.setText("Negative");

        Button btnXStickyZero = new Button(zoomingPageControls, SWT.CHECK);
        btnXStickyZero.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                zoomLayer.setStickyX(btnXStickyZero.getSelection());
            }
        });
        btnXStickyZero.setText("Sticky Zero");

        Button btnYStickyZero = new Button(zoomingPageControls, SWT.CHECK);
        btnYStickyZero.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                zoomLayer.setStickyY(btnYStickyZero.getSelection());
            }
        });
        btnYStickyZero.setText("Sticky Zero");

        org.eclipse.swt.widgets.Label lblXResizeCenter = new org.eclipse.swt.widgets.Label(zoomingPageControls, SWT.NONE);
        lblXResizeCenter.setText("Resize Center:");

        org.eclipse.swt.widgets.Label lblYResizeCenter = new org.eclipse.swt.widgets.Label(zoomingPageControls, SWT.NONE);
        lblYResizeCenter.setText("Resize Center:");

        Scale scaleXResizeCenter = new Scale(zoomingPageControls, SWT.NONE);
        scaleXResizeCenter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        scaleXResizeCenter.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                double resizeCenterX = (double) scaleXResizeCenter.getSelection() / 100.0;
                view.getViewContext().setResizeCenterX(resizeCenterX);
                view.contextUpdated();
            }
        });
        scaleXResizeCenter.setIncrement(1);
        scaleXResizeCenter.setSelection(0);

        Scale scaleYResizeCenter = new Scale(zoomingPageControls, SWT.NONE);
        scaleYResizeCenter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        scaleYResizeCenter.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                double resizeCenterY = (double) scaleYResizeCenter.getSelection() / 100.0;
                view.getViewContext().setResizeCenterY(resizeCenterY);
                view.contextUpdated();
            }
        });
        scaleYResizeCenter.setSelection(0);
        scaleYResizeCenter.setIncrement(1);

        TabItem tbtmSwitchingModel = new TabItem(tabFolder, SWT.NONE);
        tbtmSwitchingModel.setText("Switching Model");

    }

    protected void createView() {
        view = new ModelAwareView<ExampleModel>();
        view.setCanvas(zoomingPageCanvas);

        TrackerServiceLayer trackerLayer = new TrackerServiceLayer();
        DraggerServiceLayer draggerLayer = new DraggerServiceLayer(trackerLayer);
        zoomLayer = new ZoomServiceLayer();
        view.addLayer(trackerLayer);
        view.addLayer(new PanServiceLayer());
        view.addLayer(zoomLayer);
        view.addLayer(new ClickerServiceLayer(trackerLayer));
        view.addLayer(draggerLayer);

        view.addLayer(new ViewportBackgroundLayer());
        view.addLayer(new ViewportXAxisLayer());
        view.addLayer(new ViewportYAxisLayer());
        view.addLayer(new ViewportZeroMarkLayer());

        view.addModelLayer(new ExampleModelRectLayer(trackerLayer));
        view.addModelLayer(new ExampleModelDraggableRectLayer(trackerLayer, draggerLayer));
        view.addModelLayer(new ExampleModelLabelLayer(trackerLayer, draggerLayer));

        view.init();

        ViewContext viewContext = new ViewContext();
        viewContext.setMargin(20);
        view.setViewContext(viewContext);

        ExampleModel model = new ExampleModel();
        model.getRectangles().add(new FRectangle(100, 50, 100, 50));
        model.getRectangles().add(new FRectangle(300, 200, 100, 50));
        model.getDraggableRectangles().add(new FRectangle(300, 50, 100, 50));
        model.getDraggableRectangles().add(new FRectangle(100, 200, 100, 50));
        model.getLabels().add(new Label("Label 1", 500.0, 100.0));
        model.getLabels().add(new Label("Label 2", 500.0, 200.0));
        view.modelUpdated(model);
    }
}
